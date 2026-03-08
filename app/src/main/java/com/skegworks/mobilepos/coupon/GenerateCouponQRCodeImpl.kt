package com.skegworks.mobilepos.coupon

import android.graphics.Bitmap
import com.skegworks.mobilepos.barcode.BarcodeFormat
import com.skegworks.mobilepos.barcode.BarcodeGenerator

class GenerateCouponQRCodeImpl(private val barcodeGenerator: BarcodeGenerator): GenerateCouponQRCode {
    override suspend fun generateQR(size: Int, data: String): Bitmap {
        return barcodeGenerator.generateBarcode(
            data,
            BarcodeFormat.QR_CODE,
            size,
            size
        )
    }
}