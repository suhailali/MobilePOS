package com.skegworks.mobilepos.data.domain

data class AppSettings(
    val id: String,
    val invoiceCounter: Long,
    val invoiceYear: Int,
    val productSkuCounter: Long
)