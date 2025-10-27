package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.sync.SyncData

class CustomerRepositoryImpl(
    private val customerDao: CustomerDao,
    private val syncData: SyncData
) : CustomerRepository {
    override suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)
    }

    override suspend fun getCustomerById(id: String): Customer? {
        return customerDao.getCustomerById(id)
    }

    override suspend fun getAllCustomers(): List<Customer> {
        return customerDao.getAllCustomers()
    }

    override suspend fun updateCustomer(customer: Customer) {
        customerDao.updateCustomer(customer)
    }

    override suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }

    override suspend fun syncCustomer(
        customer: Customer,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = "customers",
            id = customer.id,
            data = customer,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllCustomers(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val customers = customerDao.getAllCustomers()
        for (customer in customers) {
            syncData.uploadData(
                name = "customers",
                id = customer.id,
                data = customer,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
}