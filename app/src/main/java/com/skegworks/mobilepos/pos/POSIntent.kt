package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Customer

sealed class POSIntent {
    class UpdateScanState(val scanFinished: Boolean) : POSIntent()
    object PrintInvoice: POSIntent()
    object AddProduct: POSIntent()
    object Payment: POSIntent()
    class AddCustomer(val customer: Customer): POSIntent()
}