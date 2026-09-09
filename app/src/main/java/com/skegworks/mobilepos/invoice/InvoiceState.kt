package com.skegworks.mobilepos.invoice

import android.graphics.pdf.PdfDocument
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem

data class InvoiceState(
    // list of total invoices
    val invoices: List<Invoice> = emptyList(),
    // invoice that is open now/displayed on screen
    val selectedInvoice: Invoice? = null,
    val isSyncing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // this is the list of item currently displaying under the invoice selected
    val selectedInvoiceItems: List<InvoiceItem>? = null,
    // this is items added for credit/return
    val creditNoteInvoiceItems: List<InvoiceItem>? = null,
    val quantity: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50),
    val invoiceItemForPartialQuantityUpdate: InvoiceItem? = null,
    val unsyncedInvoices: Int = 0,
    val invoicePDF: PdfDocument? = null,
    val pdfGenerated: Boolean = false,
    val isLastPage: Boolean = false,
    val currentPage: Int = 0,
    val textStatePhone: String = ""
    )
