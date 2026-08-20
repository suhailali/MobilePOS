package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.data.local.InvoiceByDay

interface DashboardRepository {
    suspend fun getInvoicesGroupByDateForWeek(): List<InvoiceByDay>
}
