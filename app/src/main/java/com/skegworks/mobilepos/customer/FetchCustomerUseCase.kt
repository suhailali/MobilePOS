package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer

interface FetchCustomerUseCase {
    suspend operator fun invoke(value: String): List<Customer>?
    suspend fun getById(value: String): Customer?
}