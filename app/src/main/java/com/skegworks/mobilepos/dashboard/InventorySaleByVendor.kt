package com.skegworks.mobilepos.dashboard

data class InventorySaleByVendor(
    val vendorId: String,
    val vendorName: String,
    val stockQuantity: Int,
    val soldQuantity: Int,
    val salesAmount: Double,
    val unsoldAmount: Double,
    val unsoldCost: Double,
    val profit: Double
)
