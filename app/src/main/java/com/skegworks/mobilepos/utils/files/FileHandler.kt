package com.skegworks.mobilepos.utils.files

import android.graphics.pdf.PdfDocument

interface FileHandler {
    suspend fun writePdfDocument(pdfDocument: PdfDocument, fileName: String): Boolean
}