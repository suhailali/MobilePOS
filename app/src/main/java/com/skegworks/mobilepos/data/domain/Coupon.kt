package com.skegworks.mobilepos.data.domain

data class Coupon(
    val id: String,
    val title: String,
    val discountCode: String,
    val description: String,
    val discountPercentage: Double,
    val discountGivenToName: String,
    val discountGivenToId: String,
    val discountType: String,
    val discountValidTill: Long,
    val discountAvailedBy: String,
    val discountAvailedOn: String,
    val discountedAmount: Double,
    val invoiceNumber: String,
    val invoiceId: String,
    val isActive: Boolean,
    val isDeleted: Boolean,
    val createdAt: Long,
    val createdBy: String,
    val updatedAt: Long,
    val updatedBy: String,
    val isSynced: Boolean
)
