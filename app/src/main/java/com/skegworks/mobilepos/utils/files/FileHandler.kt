package com.skegworks.mobilepos.utils.files

import android.graphics.pdf.PdfDocument

interface FileHandler<T> {
    suspend fun writeDocument(document: T, fileName: String): Boolean
}