package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem

data class InvoiceState(
    // list of total invoices
    val invoices: List<Invoice> = emptyList(),
    // invoice that is open now/displayed on screen
    val selectedInvoice: Invoice? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // this is the list of item currently displaying under the invoice selected
    val selectedInvoiceItems: List<InvoiceItem>? = null,
    // this is items added for credit/return
    val creditNoteInvoiceItems: List<InvoiceItem>? = null,
    val returnDescription: String = ""
)
