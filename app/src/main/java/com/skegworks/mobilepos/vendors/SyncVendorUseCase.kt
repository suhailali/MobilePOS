package com.skegworks.mobilepos.vendors

import android.util.Log
import com.skegworks.mobilepos.data.Vendor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncVendorUseCase @Inject constructor(private val repository: VendorRepository) {
    suspend operator fun invoke(vendor: Vendor) {

        repository.insertVendor(vendor)
        repository.syncVendor(
            vendor.apply { isSynced = true },
            onSuccess = { id ->
                Log.d("Firestore", "Added with ID: $id")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.updateVendor(vendor)
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