package com.skegworks.mobilepos.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.local.InvoiceByDay
import com.skegworks.mobilepos.invoice.InvoiceRepository
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
    private val invoiceRepository: InvoiceRepository,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun loadDashboardData() {
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val invoiceGrouped = dashboardRepository.getInvoicesGroupByDateForWeek()
            val total = invoiceGrouped.sumOf {
                it.totalAmount
            }
            val maxRange = invoiceGrouped.maxOf { it.totalAmount }.toInt()
            _state.update {
                it.copy(
                    //dashboardValue = total,
                    salesPerDay = getBarChartData(
                        invoiceGrouped,
                        invoiceGrouped.size,
                        maxRange,
                        BarChartType.VERTICAL,
                        DataCategorySettings()
                    ),
                    yStepSize = maxRange / 5000,
                    maxRange = maxRange,
                    totalSales = total,
                    averageSales = total / 7,
                    isLoading = false,
                )
            }
        }
    }

    fun getBarChartData(
        invoiceGrouped: List<InvoiceByDay>,
        listSize: Int,
        maxRange: Int,
        barChartType: BarChartType,
        dataCategorySettings: DataCategorySettings
    ): List<BarData> {
        val list = arrayListOf<BarData>()
        for (index in 0 until listSize) {
            val point = when (barChartType) {
                BarChartType.VERTICAL -> {
                    Point(
                        index.toFloat(),
                        invoiceGrouped[index].totalAmount.formatNumber().toFloat()
                    )
                }

                BarChartType.HORIZONTAL -> {
                    Point(
                        invoiceGrouped[index].totalAmount.formatNumber().toFloat(),
                        index.toFloat()
                    )
                }
            }

            list.add(
                BarData(
                    point = point,
                    color = getBarColor(Random.nextInt(0, 6)),
                    dataCategorySettings = dataCategorySettings,
                    label = invoiceGrouped[index].date,
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
}