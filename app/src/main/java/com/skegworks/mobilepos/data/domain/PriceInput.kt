package com.skegworks.mobilepos.data.domain

data class PriceInput(
    val itemPrice: Double,
    val inputGstPercentage: Double,
    val outputGstPercentage: Double,
    val saleMargin: Int,
    val discountPercentage: Double,
    // This field is for the coupon code discount
    val additionalDiscountPercentage: Double
)
