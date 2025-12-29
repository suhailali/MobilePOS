package com.skegworks.mobilepos.data.domain

data class CreditNote(
    val id: String,
    val invoiceId: String,
    val invoiceNumber: String,
    val invoiceDate: String,
    val totalPrice: Double,
    val items: List<CreditNoteItem>,
    val totalDiscount: Double,
    val finalPrice: Double,
    val customer: Customer,
    val business: Business,
    var isSynced: Boolean,
    val coupon: Coupon? = null,
    val cashDiscount: Double,
    var description: String,
    var creditNoteDate: String
)
