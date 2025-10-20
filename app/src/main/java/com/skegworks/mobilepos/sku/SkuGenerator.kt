package com.skegworks.mobilepos.sku

interface SkuGenerator {
    fun generateSku(category: String,
                    vendor: String,
                    title:String,
                    color: String,
                    size: String,
                    sequence: Int): String
}