package com.skegworks.mobilepos.invoice

import android.graphics.pdf.PdfDocument
import com.skegworks.mobilepos.data.domain.Invoice

interface GenerateInvoicePdfUseCase {
    suspend fun generatePdf(invoice: Invoice, infoForCustomer: Array<String>): PdfDocument
}