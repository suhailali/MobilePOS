package com.skegworks.mobilepos.pos

import android.graphics.pdf.PdfDocument
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.domain.Product
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class POSState @OptIn(ExperimentalTime::class) constructor(
    val invoiceNumber: String = "",
    val invoiceDate: String = Clock.System.now().toString(),
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
    val cashDiscount: Double = 0.0
)
