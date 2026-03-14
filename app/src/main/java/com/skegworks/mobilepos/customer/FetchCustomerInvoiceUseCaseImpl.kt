package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.invoice.InvoiceRepository
import javax.inject.Inject

class FetchCustomerInvoiceUseCaseImpl @Inject constructor(private val repository: InvoiceRepository): FetchCustomerInvoiceUseCase {
    override suspend fun invoke(customerId: String): List<Invoice>{
        return repository.getInvoicesByCustomer(customerId)
    }
}