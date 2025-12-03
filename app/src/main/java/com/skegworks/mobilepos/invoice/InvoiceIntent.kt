package com.skegworks.mobilepos.invoice

sealed class InvoiceIntent {
    object LoadInvoices : InvoiceIntent()
    data class SelectInvoice(val id: String) : InvoiceIntent()
}