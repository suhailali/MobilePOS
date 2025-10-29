package com.skegworks.mobilepos.data.domain

data class Invoice(
    val invoiceNumber: String,
    val invoiceDate: String,
    val totalPrice: Double,
    val items: List<Product>,
    val totalDiscount: Double,
    val finalPrice: Double,
    val customerID: String,
    val customerName: String,
    val customerNumber: String,
)