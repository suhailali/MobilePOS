package com.skegworks.mobilepos.customer

import android.util.Log
import com.skegworks.mobilepos.data.domain.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateCustomerUseCase @Inject constructor(private val repository: CustomerRepository) {
    suspend operator fun invoke(customer: Customer) {

        repository.updateCustomer(customer)
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