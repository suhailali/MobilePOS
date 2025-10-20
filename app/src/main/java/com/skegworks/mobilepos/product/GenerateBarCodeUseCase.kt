package com.skegworks.mobilepos.product

import android.graphics.Bitmap
import com.skegworks.mobilepos.barcode.BarcodeGenerator
import jakarta.inject.Inject

class GenerateBarCodeUseCase @Inject constructor(private val barCodeGenerator: BarcodeGenerator) {
    suspend operator fun invoke(sku: String): Bitmap {
        val barcode = barCodeGenerator.generateBarcode(
            data = sku,
            barcodeFormat = com.skegworks.mobilepos.barcode.BarcodeFormat.CODE_128,
            width = 1000,
            height = 250
        )
        return barcode
    }
}