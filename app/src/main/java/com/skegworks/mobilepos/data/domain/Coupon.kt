package com.skegworks.mobilepos.data.domain

data class Coupon(
    val id: String,
    val title: String,
    val discountCode: String,
    val description: String,
    val discountPercentage: Double,
    val discountGivenTo: String,
    val discountType: String,
    val discountValidTill: Long,
    val discountAvailedBy: String,
    val invoiceNumber: String,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean
)
