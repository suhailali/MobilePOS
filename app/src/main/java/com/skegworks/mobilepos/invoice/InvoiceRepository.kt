package com.skegworks.mobilepos.invoice

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
}