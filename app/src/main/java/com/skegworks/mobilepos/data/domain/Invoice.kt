package com.skegworks.mobilepos.data.domain

data class Invoice(
    val invoiceNumber: String,
    val invoiceDate: String,
    val totalPrice: Double,
    val items: List<InvoiceItem>,
    val totalDiscount: Double,
    val finalPrice: Double,
    val customer: Customer,
    val business: Business,
)