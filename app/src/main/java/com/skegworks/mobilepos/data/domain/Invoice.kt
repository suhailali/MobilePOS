package com.skegworks.mobilepos.data.domain

data class Invoice(
    val id: String,
    val invoiceNumber: String,
    val invoiceDate: String,
    val totalPrice: Double,
    val items: List<InvoiceItem>,
    // this field is for the total pre applied discount on products
    val totalDiscount: Double,
    val finalPrice: Double,
    val customer: Customer,
    val business: Business,
    var isSynced: Boolean,
    var coupon: Coupon? = null,
    val cashDiscount: Double,
    // this field is for coupon discounted amount
    val couponDiscount: Double,
    val invoiceState: InvoiceState,
    val updatedAt: Long,
    var isCreditNote: Boolean = false
)

enum class InvoiceState(val value:String) {
    PRINT("print"),
    PAID("paid");

    companion object {
        fun fromState(status: String): InvoiceState? {
            return InvoiceState.entries.firstOrNull { it.value.equals(status, ignoreCase = true) }
        }
    }
}