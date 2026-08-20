package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.ChartPoint
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem

interface InvoiceRepository {
    suspend fun insertInvoice(invoice: Invoice)
    suspend fun updateInvoice(invoice: Invoice)

    suspend fun syncInvoice(
        invoice: Invoice,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun insertInvoiceItem(invoiceItem: InvoiceItem)
    suspend fun updateInvoiceItem(invoiceItem: InvoiceItem)

    suspend fun syncInvoiceItem(
        invoiceItem: InvoiceItem,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun getAllInvoices(): List<Invoice>

    suspend fun getPagedInvoices(limit: Int, offset: Int): List<Invoice>

    suspend fun getInvoicesByCustomer(customerId: String): List<Invoice>
    suspend fun getUnsyncedInvoices(): List<Invoice>

    suspend fun getInvoicesAmountByDate(): List<ChartPoint>

    suspend fun getInvoiceById(id: String): Invoice?

}
