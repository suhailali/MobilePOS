package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.InvoiceItem

sealed class InvoiceIntent {
    object LoadInvoices : InvoiceIntent()
    object SyncInvoices : InvoiceIntent()
    data class CreditNoteInvoiceItem(val invoiceItem: InvoiceItem, val quantity: Int, val addItem: Boolean) : InvoiceIntent()
    data class SelectInvoice(val id: String) : InvoiceIntent()
    data class ConfirmCreditNote(val returnDescription: String) : InvoiceIntent()
    data class ItemForPartialQuantityUpdate(val invoiceItem: InvoiceItem) : InvoiceIntent()
}