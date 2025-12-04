package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

data class InvoiceState(
    val invoices: List<Invoice> = emptyList(),
    val selectedInvoice: Invoice? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
