package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.data.local.DayInvoiceAmount
import com.skegworks.mobilepos.data.local.InvoiceByDay

interface DashboardRepository {
    suspend fun getInvoicesGroupByDateForWeek(): List<InvoiceByDay>

    suspend fun getTodaySales(): List<InvoiceByDay>

    suspend fun getMonthlySale(): List<InvoiceByDay>
}
