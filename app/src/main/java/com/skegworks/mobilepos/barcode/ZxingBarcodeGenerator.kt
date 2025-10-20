package com.skegworks.mobilepos.barcode

import coil3.Bitmap
import com.journeyapps.barcodescanner.BarcodeEncoder

class ZxingBarcodeGenerator : BarcodeGenerator {
    override suspend fun generateBarcode(
        data: String,
        barcodeFormat: BarcodeFormat,
        width: Int,
        height: Int
    ): Bitmap {
        val format = when (barcodeFormat) {
            BarcodeFormat.CODE_128 -> com.google.zxing.BarcodeFormat.CODE_128
            BarcodeFormat.CODE_39 -> com.google.zxing.BarcodeFormat.CODE_39
            BarcodeFormat.EAN_13 -> com.google.zxing.BarcodeFormat.EAN_13
            BarcodeFormat.EAN_8 -> com.google.zxing.BarcodeFormat.EAN_8
            BarcodeFormat.UPC_A -> com.google.zxing.BarcodeFormat.UPC_A
            BarcodeFormat.UPC_E -> com.google.zxing.BarcodeFormat.UPC_E
            BarcodeFormat.QR_CODE -> com.google.zxing.BarcodeFormat.QR_CODE
            BarcodeFormat.PDF_417 -> com.google.zxing.BarcodeFormat.PDF_417
            BarcodeFormat.AZTEC -> com.google.zxing.BarcodeFormat.AZTEC
            BarcodeFormat.DATA_MATRIX -> com.google.zxing.BarcodeFormat.DATA_MATRIX
        }

        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(data, format, width, height)
        return bitmap
    }
}