package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.data.local.InvoiceByDay
import com.skegworks.mobilepos.invoice.InvoiceRepository
import com.skegworks.mobilepos.utils.Constants
import com.skegworks.mobilepos.utils.DateUtility
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val dateUtility: DateUtility) :
    DashboardRepository {
    override suspend fun getInvoicesGroupByDateForWeek(): List<InvoiceByDay> {
        val invoiceByDayList: MutableList<InvoiceByDay> = mutableListOf()
        val invoices = invoiceRepository.getInvoiceForLastSevenDays()
        val invoicesGroupByDate = invoices.groupBy {
            dateUtility.formatDate(
                it.invoiceDate, Constants.DateFormat.INVOICE_DATE_TIME_FORMAT,
                Constants.DateFormat.DATE_ONLY_FORMAT
            )
        }

        invoicesGroupByDate.forEach { (date, invoices) ->
            var totalAmount = 0.0
            invoices.forEach {
                totalAmount += it.finalPrice
            }
            invoiceByDayList.add(InvoiceByDay(date, invoices.size, totalAmount))
        }
        return invoiceByDayList
    }
}