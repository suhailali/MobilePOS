package com.skegworks.mobilepos.print

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import java.io.FileOutputStream
import java.io.IOException

class PrintPdfUseCaseImpl: PrintPdfUseCase {
    override fun invoke(context: Context, pdfDocument: PdfDocument, jobName: String) {
// Convert PdfDocument to ByteArray
        val outStream = java.io.ByteArrayOutputStream()
        try {
            pdfDocument.writeTo(outStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }

        val pdfBytes = outStream.toByteArray()

        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAttributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A5)      // ✅ Page size
            .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)  // ✅ Black & white
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()
        val adapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
// Respond that layout is finished
                val info = PrintDocumentInfo
                    .Builder("$jobName.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()
                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                try {
                    destination?.let { pfd ->
                        FileOutputStream(pfd.fileDescriptor).use { out ->
                            out.write(pdfBytes)
                        }
                    }
                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback?.onWriteFailed(e.message)
                }
            }
        }

        printManager.print(jobName, adapter, printAttributes)
    }
}