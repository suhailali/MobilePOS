package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

interface SyncInvoiceUseCase {
    suspend operator fun invoke(invoice: Invoice)
}