package com.skegworks.mobilepos.barcode

import android.graphics.Bitmap

interface BarcodeGenerator {
    suspend fun generateBarcode(data: String,
                                barcodeFormat: BarcodeFormat,
                                width: Int,
                                height: Int): Bitmap
}

enum class BarcodeFormat {
    CODE_128,
    CODE_39,
    EAN_13,
    EAN_8,
    UPC_A,
    UPC_E,
    QR_CODE,
    PDF_417,
    AZTEC,
    DATA_MATRIX
}