package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.invoice.InvoiceRepository
import javax.inject.Inject

class TopCustomerUseCaseImpl @Inject constructor(private val invoiceRepository: InvoiceRepository) :
    TopCustomerUseCase {
    override suspend fun invoke(): List<TopCustomer> {
        val list = invoiceRepository.getTopCustomer()
        return list
    }
}