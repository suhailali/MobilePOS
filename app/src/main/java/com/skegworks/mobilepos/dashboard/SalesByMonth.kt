package com.skegworks.mobilepos.dashboard

data class SalesByMonth(
    val numberOfInvoices: Int,
    val month: String, // monthRange
    val salesQuantity: Int, // item quantity
    val salesCost: Double, // item cost * quantity
    val salesAmount: Double, // item price * quantity
    val salesProfit: Double, // item quantity (price - cost)
    val cashDiscountGiven: Double, // invoice cash discount
    val offerDiscountGiven: Double, // invoice total discount
    val couponDiscountGiven: Double, // invoice coupon discount
    val outputGST: Double, // item output GST * quantity
)
