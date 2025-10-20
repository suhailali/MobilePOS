package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.Customer

interface CustomerRepository {
    suspend fun insertCustomer(customer: Customer)

    suspend fun getCustomerById(id: String) : Customer?

    suspend fun getAllCategories(): List<Customer>

    suspend fun updateCustomer(customer: Customer)

    suspend fun deleteCustomer(customer: Customer)
}