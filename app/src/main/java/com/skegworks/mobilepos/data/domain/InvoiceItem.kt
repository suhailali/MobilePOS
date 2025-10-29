package com.skegworks.mobilepos.data.domain

data class InvoiceItem(
    val id: String,
    val vendorId: String,
    val vendorName: String,
    val hsnCode: String,
    val title: String,
    val categoryId: String,
    val categoryName: String,
    val sku: String,
    val size: String,
    val color: String,

    val itemPrice: Double,
    val inputGstPercentage: Double,
    val inputGst: Double,
    val outputGstPercentage: Double,
    val outputGst: Double,
    val saleMargin: Int,
    val cost: Double,
    val salePriceWithoutGst: Double,
    val salePrice: Double,
    val finalRoundedOffPrice: Int,

    val quantity: Int,
    val alertQuantity: Int,
    val description: String,
    val imageUrl: String,
    val isActive: Boolean,
    var isSynced: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val discountPercentage: Double,

    val discountAppliedInPercentage: Double,
    val discountedAmount: Double,
    val priceAfterDiscount: Double,

    val productId: String,
    val invoiceId: String,
    val invoiceNumber: String,
    val invoiceDate: Long,
)