package com.skegworks.mobilepos.invoice

sealed class InvoiceIntent {
    object LoadInvoices : InvoiceIntent()
    object SyncInvoices : InvoiceIntent()
    data class SelectInvoice(val id: String) : InvoiceIntent()
}