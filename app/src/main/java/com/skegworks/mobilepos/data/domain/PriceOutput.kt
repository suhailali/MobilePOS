package com.skegworks.mobilepos.data.domain

data class PriceOutput(
    val inputGst: Double,
    val cost: Double,
    val salePriceBeforeGst: Double,
    val outputGst: Double,
    val priceAfterDiscountWithoutGst: Double,
    val discountAmount: Double,
    val salePrice: Double,
    val priceWithoutDiscount: Int,
    val finalRoundedOffPrice: Int
)
