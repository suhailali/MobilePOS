package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer

interface CustomerRepository {
    suspend fun insertCustomer(customer: Customer)

    suspend fun getCustomerById(id: String) : Customer?

    suspend fun getAllCustomers(): List<Customer>

    suspend fun getCustomersByPhone(phone: String): List<Customer>?

    suspend fun updateCustomer(customer: Customer)

    suspend fun deleteCustomer(customer: Customer)

    suspend fun syncCustomer(
        customer: Customer,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun syncAllCustomers(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}