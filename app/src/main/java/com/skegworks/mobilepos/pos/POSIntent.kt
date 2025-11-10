package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.domain.Product

sealed class POSIntent {
    class UpdateScanState(val scanFinished: Boolean) : POSIntent()
    object PrintInvoice : POSIntent()
    class AddProduct(val product: Product) : POSIntent()
    object Payment : POSIntent()
    class AddCustomer(val customer: Customer) : POSIntent()
    class AddCoupon(val coupon: Coupon) : POSIntent()
    class RemoveItem(val invoiceItem: InvoiceItem) : POSIntent()
    object SearchItem : POSIntent()
    class UpdateSearchTerm(val term: String) : POSIntent()
    class AddCashDiscount(val cashDiscount: Double) : POSIntent()
    object RemoveCoupon : POSIntent()
}