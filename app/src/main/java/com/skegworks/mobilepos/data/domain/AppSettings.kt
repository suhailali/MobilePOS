package com.skegworks.mobilepos.data.domain

data class AppSettings(
    val id: Int,
    val invoiceCounter: Long,
    val invoiceYear: Int,
    val productSkuCounter: Long
)