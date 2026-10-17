package com.skegworks.mobilepos.sync

import android.util.Log
import com.skegworks.mobilepos.customer.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncPendingCustomersUseCaseImpl @Inject constructor(private val repository: CustomerRepository) :
    SyncPendingCustomersUseCase {
    override suspend fun invoke() {
        val customers = repository.getUnsyncedCustomers()
        customers.forEach { customer ->
            repository.syncCustomer(
                customer.apply { isSynced = true },
                onSuccess = { id ->
                    Log.d("Firestore", "Added with ID: $id")
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.updateCustomer(customer)
                    }
                },
                onFailure = { exception ->
                    Log.e(
                        "Firestore",
                        "Error adding document",
                        exception
                    )
                }
            )
        }
    }
}