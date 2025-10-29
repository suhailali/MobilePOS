package com.skegworks.mobilepos.pos

sealed class POSIntent {
    class UpdateScanState(val scanFinished: Boolean) : POSIntent()
    object PrintInvoice: POSIntent()
    object AddProduct: POSIntent()
}