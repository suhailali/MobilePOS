package com.skegworks.mobilepos.data.local

data class InvoiceByDay(
    val date: String,
    val noOfInvoices: Int,
    val totalAmount: Double,
)
