package com.skegworks.mobilepos.pos

import android.graphics.pdf.PdfDocument
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.domain.Product

data class POSState(
    val invoiceNumber: String = "",
    val invoiceDate: String = "",
    val product: Product? = null,
    val invoiceItems: List<InvoiceItem> = emptyList(),

    val productFound: Boolean = false,
    val searchingProduct: Boolean = false,
    val totalPrice: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val finalPriceToPay: Double = 0.0,
    val invoicePDF: PdfDocument? = null,
    val pdfGenerated: Boolean = false,
    val invoice: Invoice? = null,
    val customer: Customer? = null,
    val cashDiscount: Double = 0.0,
    val searchTerm: String = "",
    val searchResultProduct: List<Product> = emptyList(),
    val searchResultCoupon: List<Coupon> = emptyList(),
)
