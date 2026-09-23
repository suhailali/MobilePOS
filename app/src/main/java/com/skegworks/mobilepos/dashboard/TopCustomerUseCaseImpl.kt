package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.invoice.InvoiceRepository
import javax.inject.Inject

class TopCustomerUseCaseImpl @Inject constructor(private val invoiceRepository: InvoiceRepository) :
    TopCustomerUseCase {
    override suspend fun invoke(): List<TopCustomer> {
        // TODO combine phone numbers if duplicate, reduce credit note
        val list = invoiceRepository.getTopCustomer()
        return list
    }
}