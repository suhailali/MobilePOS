package com.skegworks.mobilepos.barcode

import coil3.Bitmap
import com.journeyapps.barcodescanner.BarcodeEncoder
import jakarta.inject.Inject

class ZxingBarcodeGenerator @Inject constructor(): BarcodeGenerator {
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

//        val size = 512 //pixels
//        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
//            data,
//            com.google.zxing.BarcodeFormat.QR_CODE,  // 👈 change format here
//            size,
//            size
//        )
//
//        val bitmap = createBitmap(size, size, android.graphics.Bitmap.Config.RGB_565)
//        for (x in 0 until size) {
//            for (y in 0 until size) {
//                bitmap[x, y] =
//                    if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
//            }
//        }
        return bitmap
    }
}