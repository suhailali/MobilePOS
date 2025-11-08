package com.skegworks.mobilepos.data.remote.firestore

data class CouponFireStoreDto(
    val id: String = "",
    val title: String = "",
    val discountCode: String = "",
    val description: String = "",
    val discountPercentage: Double = 0.0,
    val discountGivenTo: String = "",
    val discountType: String = "",
    val discountValidTill: Long = 0L,
    val discountAvailedBy: String = "",
    val invoiceNumber: String ="",
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isSynced: Boolean = true
)
