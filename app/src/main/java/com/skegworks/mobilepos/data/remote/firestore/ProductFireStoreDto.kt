package com.skegworks.mobilepos.data.remote.firestore

data class ProductFireStoreDto(
    val id: String = "",
    val vendorId: String = "",
    val vendorName: String = "",
    val hsnCode: String = "",
    val title: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val sku: String = "",
    val size: String = "",
    val color: String = "",

    val itemPrice: Double = 0.0,
    val inputGstPercentage: Double = 0.0,
    val inputGst: Double = 0.0,
    val outputGstPercentage: Double = 0.0,
    val outputGst: Double = 0.0,
    val saleMargin: Int = 0,
    val cost: Double = 0.0,
    val salePriceWithoutGst: Double = 0.0,
    val salePrice: Double = 0.0,
    val salePriceWithoutDiscount: Int = 0,
    val finalRoundedOffPrice: Int = 0,

    val quantity: Int = 0,
    val alertQuantity: Int = 0,
    val description: String = "",
    val imageUrl: String = "",
    val isActive: Boolean = true,
    var isSynced: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val discountPercentage: Double = 0.0,
    val discountAmount: Double = 0.0,
    val createdBy: String = "",
    val updatedBy: String = ""
)
