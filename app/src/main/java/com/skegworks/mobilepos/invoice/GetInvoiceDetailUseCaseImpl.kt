package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

class GetInvoiceDetailUseCaseImpl(private val invoiceRepository: InvoiceRepository) :
    GetInvoiceDetailUseCase {
    override suspend fun invoke(invoiceId: String): Invoice? {
        return invoiceRepository.getInvoiceById(id = invoiceId)
    }
}