package com.skegworks.mobilepos.utils.files

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.provider.MediaStore
import kotlinx.io.IOException
import javax.inject.Inject

class FileHandlerImpl @Inject constructor(private val context: Context): FileHandler {
    override suspend fun writePdfDocument(pdfDocument: PdfDocument): Boolean {
        val resolver = context.contentResolver
        val fileName = "sample_file_${System.currentTimeMillis()}"
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.pdf")
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")

            // 👇 This automatically creates Downloads/MyApp folder
            put(MediaStore.Downloads.RELATIVE_PATH, "Download/MyApp/")
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()

        } finally {
            pdfDocument.close()
        }
        return true
    }
}