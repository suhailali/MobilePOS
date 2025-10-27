package com.skegworks.mobilepos.product

import android.util.Log
import com.skegworks.mobilepos.data.domain.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncProductUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product) {

        repository.insertProduct(product)
        repository.syncProduct(
            product.apply { isSynced = true },
            onSuccess = { id ->
                Log.d("Firestore", "Added with ID: $id")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.updateProduct(product)
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