package com.skegworks.mobilepos.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.domain.ChartPoint
import com.skegworks.mobilepos.data.local.InvoiceByDay
import com.skegworks.mobilepos.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.chaintech.cmpcharts.axis.DataCategorySettings
import network.chaintech.cmpcharts.common.extensions.formatNumber
import network.chaintech.cmpcharts.ui.barchart.config.BarChartType
import network.chaintech.cmpcharts.ui.barchart.config.BarData
import network.chaintech.cmpcharts.common.model.Point
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val productRepository: ProductRepository,
    private val dashboardRepository: DashboardRepository,
    private val inventorySalesByVendorUseCase: InventorySalesByVendorUseCase,
    private val salesByMonthUseCase: SalesByMonthUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun loadDashboardData() {
        _state.update {
            it.copy(isLoading = true)
        }
        getTodaySale()
    }

    private fun getInventorySalesByVendor() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = inventorySalesByVendorUseCase()
            _state.update {
                it.copy(
                    inventorySalesByVendor = list,
                    totalInventorySales = getTotalInventorySales(list)
                )
            }
        }
    }

    private fun getTotalInventorySales(list: List<InventorySaleByVendor>): InventorySaleByVendor {
        var stockQuantity = 0
        var stockCost = 0.0
        var stockPrice = 0.0
        var saleQuantity = 0.0
        var saleAmount = 0.0
        var saleProfit = 0.0
        list.forEach {
            stockQuantity += it.stockQuantity
            stockCost += it.unsoldCost
            stockPrice += it.unsoldAmount
            saleQuantity += it.soldQuantity
            saleAmount += it.salesAmount
            saleProfit += it.profit
        }
        return InventorySaleByVendor(
            "total",
            "Total",
            stockQuantity,
            saleQuantity.toInt(),
            saleAmount,
            stockPrice,
            stockCost,
            saleProfit
        )
    }

    private fun convertToChartData(invoiceGrouped: List<InvoiceByDay>): List<ChartPoint> {
        val list = arrayListOf<ChartPoint>()
        invoiceGrouped.forEach {
            list.add(ChartPoint(it.dateOrNumber, it.totalAmount))
        }
        return list
    }
    
    private fun createChartData(invoiceGrouped: List<InvoiceByDay>) {
        if (invoiceGrouped.isEmpty()) {
            _state.update {
                it.copy(
                    salesPerDay = emptyList(),
                    yStepSize = 0,
                    maxRange = 0,
                    totalSales = 0.0,
                    averageSales = 0.0,
                    isLoading = false,
                )
            }
            return
        }
        val chartData = convertToChartData(invoiceGrouped)
        val total = chartData.sumOf {
            it.yValue
        }
        val maxRange = chartData.maxOf { it.yValue }.toInt()
        _state.update {
            it.copy(
                //dashboardValue = total,
                salesPerDay = getBarChartData(
                    chartData,
                    chartData.size,
                    BarChartType.VERTICAL,
                    DataCategorySettings()
                ),
                yStepSize = maxRange / getStepSizeDivider(maxRange),
                maxRange = maxRange,
                totalSales = total,
                averageSales = total / 7,
                isLoading = false,
            )
        }
    }

    private fun getStepSizeDivider(maxRange: Int) : Int {
        var step = 5000
        if (maxRange > 100000) {
            step = 20000
        } else if (maxRange > 50000) {
            step = 10000
        } else if (maxRange > 25000) {
            step = 4000
        } else if (maxRange > 10000) {
            step = 2000
        } else if (maxRange > 5000) {
            step = 1000
        } else if (maxRange > 2500) {
            step = 400
        } else if (maxRange > 1000) {
            step = 200
        }
        return step
    }

    private fun getBarChartData(
        invoiceGrouped: List<ChartPoint>,
        listSize: Int,
        barChartType: BarChartType,
        dataCategorySettings: DataCategorySettings
    ): List<BarData> {
        val list = arrayListOf<BarData>()
        for (index in 0 until listSize) {
            val point = when (barChartType) {
                BarChartType.VERTICAL -> {
                    Point(
                        index.toFloat(),
                        invoiceGrouped[index].yValue.formatNumber().toFloat()
                    )
                }

                BarChartType.HORIZONTAL -> {
                    Point(
                        invoiceGrouped[index].yValue.formatNumber().toFloat(),
                        index.toFloat()
                    )
                }
            }

            list.add(
                BarData(
                    point = point,
                    color = getBarColor(Random.nextInt(0, 6)),
                    dataCategorySettings = dataCategorySettings,
                    label = invoiceGrouped[index].xValue,
                )
            )
        }
        return list
    }

    private fun getBarColor(randomIndex: Int = 5): Color {
        return listColors[randomIndex]
    }

    val listColors = listOf(
        Color.Gray,
        Color.Red,
        Color.Blue,
        Color.Yellow,
        Color.Cyan,
        Color.Green,
        Color.LightGray
    )

    fun selectDateFilter(filter: DateFilter) {
        _state.update {
            it.copy(dateFilterSelected = filter)
        }
        when (filter) {
            DateFilter.TODAY -> {
                getTodaySale()
            }
            DateFilter.WEEK -> {
                getWeekSale()
            }
            DateFilter.THIS_MONTH -> {
                getThisMonthSale()
            }
            else -> {

            }
        }
    }

    fun selectCategoryFilter(filter: CategoryFilter) {
        _state.update {
            it.copy(categoryFilterSelected = filter)
        }
    }
    
    fun getTodaySale() {
        viewModelScope.launch(Dispatchers.IO) { 
            val sales = dashboardRepository.getTodaySales()
            createChartData(sales)
        }
    }

    fun getWeekSale() {
        viewModelScope.launch(Dispatchers.IO) {
            val invoiceGrouped = dashboardRepository.getInvoicesGroupByDateForWeek()
            createChartData(invoiceGrouped)
        }
    }

    fun getThisMonthSale() {
        viewModelScope.launch(Dispatchers.IO) {
            val invoiceGrouped = dashboardRepository.getMonthlySale()
            createChartData(invoiceGrouped)
        }
    }

    fun loadInventorySalesByVendor() {
        getInventorySalesByVendor()
    }

    fun loadProductForVendor(vendorId: String) {
        viewModelScope.launch {
            val products = productRepository.getProductForVendor(vendorId)
            _state.update {
                it.copy(productList = products)
            }
        }
    }

    fun loadSaleByMonth() {
        viewModelScope.launch(Dispatchers.IO) {
            val sales = salesByMonthUseCase()
            _state.update {
                it.copy(salesByMonth = sales)
            }
        }
    }
}