package com.skegworks.mobilepos.dashboard

data class TopCustomer(
    val id: String,
    val name: String,
    val phone: String,
    val totalSpend: Double,
    val totalVisits: Int,
    val totalReturns: Int,
    val maxSpend: Double,
    val totalDiscountReceived: Double
)
