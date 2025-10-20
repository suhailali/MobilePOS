package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.sku.SkuGenerator
import jakarta.inject.Inject

class GenerateSkuUseCase @Inject constructor(
    private val skuGenerator: SkuGenerator
) {
    operator fun invoke(
        category: String,
        vendor: String,
        title: String,
        color: String,
        size: String,
        sequence: Int
    ): String {
        return skuGenerator.generateSku(
            category,
            vendor,
            title,
            color,
            size,
            sequence
        )
    }
}