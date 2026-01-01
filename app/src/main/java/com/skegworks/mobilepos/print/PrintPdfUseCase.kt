package com.skegworks.mobilepos.print

import android.content.Context
import android.graphics.pdf.PdfDocument

interface PrintPdfUseCase {
    operator fun invoke(context: Context, pdfDocument: PdfDocument, jobName: String)
}