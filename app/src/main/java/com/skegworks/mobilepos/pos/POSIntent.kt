package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.domain.InvoiceItem

sealed class POSIntent {
    class UpdateScanState(val scanFinished: Boolean) : POSIntent()
    object PrintInvoice: POSIntent()
    object AddProduct: POSIntent()
    object Payment: POSIntent()
    class AddCustomer(val customer: Customer): POSIntent()
    class RemoveItem(val invoiceItem: InvoiceItem): POSIntent()
}