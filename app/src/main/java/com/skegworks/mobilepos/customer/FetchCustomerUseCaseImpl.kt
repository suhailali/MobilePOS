package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer
import javax.inject.Inject

class FetchCustomerUseCaseImpl @Inject constructor(private val repository: CustomerRepository): FetchCustomerUseCase {
    override suspend fun invoke(value: String): List<Customer>? {
        return repository.getCustomersByPhoneOrName(value)
    }
}