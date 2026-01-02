package com.skegworks.mobilepos.data.remote.firestore

data class InvoiceFireStoreDto(
    val id: String = "",
    val invoiceNumber: String = "",
    val invoiceDate: String = "",
    val totalPrice: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val finalPrice: Double = 0.0,
    val cashDiscount: Double = 0.0,
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val businessId: String = "",
    val invoiceState: String = "",
    val couponId: String = "",
    val isSynced: Boolean = false,
    val updatedAt: Long = 0L
)