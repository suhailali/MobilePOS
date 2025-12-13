package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

interface GetInvoiceDetailUseCase {
    suspend operator fun invoke(invoiceId: String): Invoice?
}