package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData

class CustomerRepositoryImpl(
    private val customerDao: CustomerDao,
    private val syncData: SyncData
) : CustomerRepository {
    override suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer.toEntity())
    }

    override suspend fun getCustomerById(id: String): Customer? {
        val cus = customerDao.getCustomerById(id)
        println("Jaggu " + cus?.name)
        return cus?.toDomain()
    }

    override suspend fun getAllCustomers(): List<Customer> {
        return customerDao.getAllCustomers().map {
            it.toDomain()
        }
    }

    override suspend fun getCustomersByPhone(phone: String): List<Customer>? {
        return customerDao.getCustomersByPhone(phone).map {
            it.toDomain()
        }
    }

    override suspend fun getCustomersByPhoneOrName(value: String): List<Customer>? {
        return customerDao.getCustomersByPhoneOrName(value).map {
            it.toDomain()
        }
    }

    override suspend fun updateCustomer(customer: Customer) {
        customerDao.updateCustomer(customer.toEntity())
    }

    override suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer.toEntity())
    }

    override suspend fun syncCustomer(
        customer: Customer,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = "customers",
            id = customer.id,
            data = customer.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllCustomers(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        //TODO transform data if required
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