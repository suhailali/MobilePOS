package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

class GetInvoicesUseCaseImpl(private val invoiceRepository: InvoiceRepository): GetInvoicesUseCase {
    override suspend fun invoke(limit: Int, offset: Int, searchValue: String): List<Invoice> {
        return invoiceRepository.getPagedInvoices(limit, offset, searchValue)
    }
}