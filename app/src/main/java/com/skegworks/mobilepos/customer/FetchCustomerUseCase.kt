package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer

interface FetchCustomerUseCase {
    suspend operator fun invoke(phone: String): List<Customer>?
}