package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.Customer

class CustomerRepositoryImpl(private val customerDao: CustomerDao): CustomerRepository {
    override suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)
    }

    override suspend fun getCustomerById(id: String): Customer? {
        return customerDao.getCustomerById(id)
    }

    override suspend fun getAllCategories(): List<Customer> {
        return customerDao.getAllCategories()
    }

    override suspend fun updateCustomer(customer: Customer) {
        customerDao.updateCustomer(customer)
    }

    override suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }
}