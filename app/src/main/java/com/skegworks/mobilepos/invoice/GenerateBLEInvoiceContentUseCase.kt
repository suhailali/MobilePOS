package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

interface GenerateBLEInvoiceContentUseCase {
    suspend operator fun invoke(invoice: Invoice, infoForCustomer: Array<String>): String
}