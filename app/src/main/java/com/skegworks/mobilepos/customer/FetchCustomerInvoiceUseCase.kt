package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Invoice

interface FetchCustomerInvoiceUseCase {
    suspend operator fun invoke(customerId: String): List<Invoice>
}