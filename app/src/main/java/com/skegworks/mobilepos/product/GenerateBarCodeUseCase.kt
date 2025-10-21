package com.skegworks.mobilepos.product

import android.graphics.Bitmap
import com.skegworks.mobilepos.barcode.BarcodeGenerator
import com.skegworks.mobilepos.utils.bitmap.BarcodeBitmapEditorImpl
import jakarta.inject.Inject

class GenerateBarCodeUseCase @Inject constructor(private val barCodeGenerator: BarcodeGenerator) {
    suspend operator fun invoke(sku: String): Bitmap {
        val barcode = barCodeGenerator.generateBarcode(
            data = sku,
            barcodeFormat = com.skegworks.mobilepos.barcode.BarcodeFormat.CODE_128,
            width = 1000,
            height = 250
        )
        val barcodeBitmap = BarcodeBitmapEditorImpl(
            "NADHIKA",
            sku,
            "1000",
        ).editBitmap(1000, 250, barcode)
        return barcodeBitmap
    }
}