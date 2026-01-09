package com.skegworks.mobilepos.data.remote.firestore

data class CreditNoteFireStoreDto(
    val id: String = "",
    val invoiceId: String = "",
    val invoiceNumber: String = "",
    val invoiceDate: String = "",
    val totalPrice: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val finalPrice: Double = 0.0,
    val cashDiscount: Double = 0.0,
    val couponDiscount: Double = 0.0,
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val businessId: String = "",
    val invoiceState: String = "",
    val couponId: String = "",
    val synced: Boolean = false,
    val description: String = "",
    val creditNoteDate: String = "",
    val updatedAt: Long = 0L
)