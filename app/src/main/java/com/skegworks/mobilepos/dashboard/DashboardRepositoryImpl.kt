package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.data.local.InvoiceByDay
import com.skegworks.mobilepos.invoice.InvoiceRepository
import com.skegworks.mobilepos.utils.Constants
import com.skegworks.mobilepos.utils.DateUtility
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val dateUtility: DateUtility
) :
    DashboardRepository {
    override suspend fun getInvoicesGroupByDateForWeek(): List<InvoiceByDay> {
        val invoiceByDayList: MutableList<InvoiceByDay> = mutableListOf()
        val invoices = invoiceRepository.getInvoiceForLastSevenDays()

        // groups all the invoices by date Map<date, list<invoices>>
        val invoicesGroupByDate = invoices.groupBy {
            dateUtility.formatDate(
                it.invoiceDate, Constants.DateFormat.INVOICE_DATE_TIME_FORMAT,
                Constants.DateFormat.DATE_ONLY_FORMAT
            )
        }

        val today = LocalDate.now()

        val formatter = DateTimeFormatter.ofPattern(Constants.DateFormat.DATE_ONLY_FORMAT)
        // Generates a list of the last 7 days ending with today
        val weekDates = (0..6).map { daysAgo ->
            today.minusDays(daysAgo.toLong()).format(formatter)
        }

        // loop the invoices for each date and calculate the total amount
        invoicesGroupByDate.forEach { (date, invoices) ->
            var totalAmount = 0.0
            invoices.forEach {
                totalAmount += it.finalPrice
            }
            invoiceByDayList.add(InvoiceByDay(date, totalAmount))
        }

        weekDates.forEach {
            if (!invoiceByDayList.any { invoiceByDay -> invoiceByDay.dateOrNumber == it }) {
                invoiceByDayList.add(InvoiceByDay(it, 0.0))
            }
        }

        // reverse the list to show the latest date first
        return invoiceByDayList.sortedBy { it.dateOrNumber }
    }

    override suspend fun getTodaySales(): List<InvoiceByDay> {
        val invoices = invoiceRepository.getInvoiceForToday()
        val todayInvoices = invoices.map {
            InvoiceByDay(
                it.invoiceNumber,
                it.finalPrice
            )
        }
        return todayInvoices
    }

    override suspend fun getMonthlySale(): List<InvoiceByDay> {
        val invoiceByDayList: MutableList<InvoiceByDay> = mutableListOf()
        val invoices = invoiceRepository.getInvoiceForThisMonth()
        val invoicesGroupByWeek = invoices.groupBy {
            when (it.invoiceDate.split("/")[0].toInt()) {
                in 1..7 -> "1 to 7"
                in 8..14 -> "8 to 14"
                in 15..21 -> "15 to 21"
                else -> "22 to END"

            }
        }
        invoicesGroupByWeek.forEach { (date, invoices) ->
            var totalAmount = 0.0
            invoices.forEach {
                totalAmount += it.finalPrice
            }
            invoiceByDayList.add(InvoiceByDay(date, totalAmount))
        }
        return invoiceByDayList.reversed()
    }
}