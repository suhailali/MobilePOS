package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

class GetInvoicesUseCaseImpl(private val invoiceRepository: InvoiceRepository): GetInvoicesUseCase {
    override suspend fun invoke(): List<Invoice> {
        return invoiceRepository.getAllInvoices()
    }
}