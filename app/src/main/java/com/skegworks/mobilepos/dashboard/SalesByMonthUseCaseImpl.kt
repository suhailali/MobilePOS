package com.skegworks.mobilepos.dashboard

import android.util.Log
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.invoice.InvoiceRepository
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

class SalesByMonthUseCaseImpl @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) : SalesByMonthUseCase {
    override suspend fun invoke(): List<SalesByMonth> {
        val sales = invoiceRepository.getAllInvoices().sortedBy { it.updatedAt }
        val monthRanges = getMonthRangesFromDecember2025()
        var currentMonthRangeIndex = 0
        var currentMonthRange = monthRanges[currentMonthRangeIndex]
        val mapSales = mutableMapOf<YearMonth, SalesByMonth>()
        sales.forEach {
            if (it.updatedAt < currentMonthRange.endMillisExclusive) {
                val sale = mapSales[currentMonthRange.month]
                if (sale == null) {
                    mapSales[currentMonthRange.month] = getSalesByMonth(it, currentMonthRange.month)
                } else {
                    val newSale = addSalesByMonth(it, currentMonthRange.month, sale)
                    mapSales[currentMonthRange.month] = newSale
                }
            } else {
                Log.w("SalesByMonthUseCaseImpl", "Switch Month:")
                currentMonthRangeIndex++
                currentMonthRange = monthRanges[currentMonthRangeIndex]
                val sale = mapSales[currentMonthRange.month]
                if (sale == null) {
                    mapSales[currentMonthRange.month] = getSalesByMonth(it, currentMonthRange.month)
                } else {
                    val newSale = addSalesByMonth(it, currentMonthRange.month, sale)
                    mapSales[currentMonthRange.month] = newSale
                }
            }
        }

        return mapSales.values.toList()
    }

    private fun addSalesByMonth(
        invoice: Invoice,
        month: YearMonth,
        previousSalesByMonth: SalesByMonth
    ): SalesByMonth {
        Log.e("SalesByMonthUseCaseImpl", "addSalesByMonth: ${invoice.invoiceNumber}")
        val salesByMonth = getSalesByMonth(invoice, month)
        return SalesByMonth(
            numberOfInvoices = previousSalesByMonth.numberOfInvoices + 1,
            month = salesByMonth.month,
            salesQuantity = previousSalesByMonth.salesQuantity + salesByMonth.salesQuantity,
            salesCost = previousSalesByMonth.salesCost + salesByMonth.salesCost,
            salesAmount = previousSalesByMonth.salesAmount + salesByMonth.salesAmount,
            salesProfit = previousSalesByMonth.salesProfit + salesByMonth.salesProfit,
            cashDiscountGiven = previousSalesByMonth.cashDiscountGiven + salesByMonth.cashDiscountGiven,
            offerDiscountGiven = previousSalesByMonth.offerDiscountGiven + salesByMonth.offerDiscountGiven,
            couponDiscountGiven = previousSalesByMonth.couponDiscountGiven + salesByMonth.couponDiscountGiven,
            outputGST = previousSalesByMonth.outputGST + salesByMonth.outputGST
        )
    }

    private fun getSalesByMonth(invoice: Invoice, month: YearMonth): SalesByMonth {
        Log.d("SalesByMonthUseCaseImpl", "getSalesByMonth: ${invoice.invoiceNumber}")
        var quantity = 0
        var saleCost = 0.0
        var saleAmount = 0.0
        var saleProfit = 0.0
        var outputGST = 0.0
        invoice.items.forEach { item ->
            quantity += item.quantity
            saleCost += item.cost * item.quantity
            saleAmount += (item.finalRoundedOffPrice * item.quantity).toDouble()
            saleProfit += (item.finalRoundedOffPrice - item.cost) * item.quantity
            outputGST += item.outputGst * item.quantity
        }
//        println("quantity " + quantity)
//        println("saleCost " + saleCost)
//        println("saleAmount " + saleAmount)
//        println("saleProfit " + saleProfit)
//        println("outputGST " + outputGST)
        return SalesByMonth(
            numberOfInvoices = 1,
            month = month.toString(),
            salesQuantity = quantity,
            salesCost = saleCost,
            salesAmount = saleAmount,
            salesProfit = saleProfit,
            cashDiscountGiven = invoice.cashDiscount,
            offerDiscountGiven = invoice.totalDiscount,
            couponDiscountGiven = invoice.couponDiscount,
            outputGST = outputGST
        )
    }

    private fun getMonthRangesFromDecember2025(): List<MonthRange> {
        val startMonth = YearMonth.of(2025, 12)
        val currentMonth = YearMonth.now()
        val zoneId = ZoneId.systemDefault()

        val months = mutableListOf<MonthRange>()
        var month = startMonth

        while (month <= currentMonth) {

            val startMillis = month
                .atDay(1)
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()

            val endMillisExclusive = month
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()

            months.add(
                MonthRange(
                    month = month,
                    startMillis = startMillis,
                    endMillisExclusive = endMillisExclusive
                )
            )

            month = month.plusMonths(1)
        }

        return months
    }

}

data class MonthRange(
    val month: YearMonth,
    val startMillis: Long,
    val endMillisExclusive: Long
)