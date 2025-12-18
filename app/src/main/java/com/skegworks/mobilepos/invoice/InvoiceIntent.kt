package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.InvoiceItem

sealed class InvoiceIntent {
    object LoadInvoices : InvoiceIntent()
    object SyncInvoices : InvoiceIntent()
    data class CreditNoteInvoiceItem(val invoiceItem: InvoiceItem, val addItem: Boolean) : InvoiceIntent()
    data class SelectInvoice(val id: String) : InvoiceIntent()
    object ConfirmCreditNote : InvoiceIntent()
}