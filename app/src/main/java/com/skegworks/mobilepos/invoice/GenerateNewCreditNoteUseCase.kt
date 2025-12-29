package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem

interface GenerateNewCreditNoteUseCase {
    suspend operator fun invoke(
        invoice: Invoice,
        invoiceItems: List<InvoiceItem>,
        returnDescription: String
    )
}