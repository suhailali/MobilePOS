package com.skegworks.mobilepos.data.remote.firestore

data class CouponFireStoreDto(
    val id: String = "",
    val title: String = "",
    val discountCode: String = "",
    val description: String = "",
    val discountPercentage: Double = 0.0,
    val discountGivenToName: String = "",
    val discountGivenToId: String = "",
    val discountGivenToNumber: String = "",
    val discountType: String = "",
    val discountValidTill: Long = 0L,
    val discountAvailedBy: String = "",
    val discountAvailedOn: String = "",
    val discountedAmount: Double = 0.0,
    val invoiceNumber: String = "",
    val invoiceId: String = "",
    val active: Boolean = false,
    val deleted: Boolean = false,
    val createdAt: Long = 0L,
    val createdBy: String = "",
    val updatedAt: Long = 0L,
    val updatedBy: String = "",
    val synced: Boolean = false
)
