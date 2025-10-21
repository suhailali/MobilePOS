package com.skegworks.mobilepos.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import androidx.core.graphics.scale
import java.io.FileOutputStream

class PdfGeneratorImpl: PdfGenerator {
    override suspend fun generatePdf(imageBitmaps: List<Bitmap>): PdfDocument {
        // Implementation here
        val pdfDocument = PdfDocument()
        //2480 x 3508 (A4 size at 300dpi
        //595 x 842 (A4 size at 72dpi
        val pageWidth = 2480
        val pageHeight = 3508
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val columns = 4
        val rows = 10
        val totalImages = columns * rows
        val cellWidth = pageWidth / columns
        val cellHeight = pageHeight / rows

        for (index in 0 until totalImages) {
            if (index >= imageBitmaps.size) break

            val image = imageBitmaps[index]
            val scaledBitmap = image.scale(cellWidth, cellHeight)

            val col = index % columns
            val row = index / columns

            val x = col * cellWidth
            val y = row * cellHeight

            canvas.drawBitmap(scaledBitmap, x.toFloat(), y.toFloat(), null)
        }
        pdfDocument.finishPage(page)
        return pdfDocument
    }
}