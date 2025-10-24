package com.skegworks.mobilepos.customer

import android.util.Log
import com.skegworks.mobilepos.data.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncCustomerUseCase @Inject constructor(private val repository: CustomerRepository) {
    suspend operator fun invoke(customer: Customer) {

        repository.insertCustomer(customer)
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