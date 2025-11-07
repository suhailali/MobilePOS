package com.skegworks.mobilepos.data.domain

data class AppSettings(
    val id: String,
    val invoiceCounter: Long,
    val invoiceYear: Int,
    val productSkuCounter: Long,
    val isSynced: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val updatedBy: String
)