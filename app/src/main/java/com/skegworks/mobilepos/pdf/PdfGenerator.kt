package com.skegworks.mobilepos.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument

interface PdfGenerator {
    suspend fun generatePdf(imageBitmaps: List<Bitmap>): PdfDocument
}