package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem

data class InvoiceState(
    val invoices: List<Invoice> = emptyList(),
    val selectedInvoice: Invoice? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedInvoiceItems: List<InvoiceItem>? = null,
    val creditNoteInvoiceItems: List<InvoiceItem>? = null
)
