package com.skegworks.mobilepos.sku

class SkuGeneratorImpl : SkuGenerator {
    override fun generateSku(
        category: String,
        vendor: String,
        title: String,
        color: String,
        size: String,
        sequence: Int
    ): String {
        val categoryCode = category.take(4).uppercase()
        val titleCode = title.replace("\\s+".toRegex(), "").take(4).uppercase()
        val sequenceCode = sequence.toString().padStart(6, '0')
        return "$categoryCode$titleCode$sequenceCode"
    }
}