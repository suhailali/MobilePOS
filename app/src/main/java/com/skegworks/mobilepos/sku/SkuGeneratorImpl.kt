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
        val categoryCode = category.take(3).uppercase()
        val vendorCode = vendor.take(3).uppercase()
        val titleCode = title.take(3).uppercase()
        val colorCode = color.take(3).uppercase()
        val sizeCode =
            size.takeIf { it.length > 1 }?.take(2)?.uppercase() ?: (size.take(1).uppercase() + "T")

        val sequenceCode = sequence.toString().padStart(6, '0')

        return "$categoryCode$vendorCode$titleCode$colorCode$sizeCode$sequenceCode"
    }
}