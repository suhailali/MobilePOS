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
            width = 620,
            height = 250
        )
        // 2480 * 3508 (A4 size at 300dpi
        // 2480 / 5 = 496
        // 3508 / 8 = 438

        // 2480 * 3508 (A4 size at 300dpi
        // 2480 / 4 = 620
        // 3508 / 10 = 350.8
        val barcodeBitmap = BarcodeBitmapEditorImpl(
            "NADHIKA",
            sku,
            "1000",
        ).editBitmap(620, 350, barcode)
        return barcodeBitmap
    }
}