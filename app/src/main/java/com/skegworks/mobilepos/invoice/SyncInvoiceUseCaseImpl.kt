package com.skegworks.mobilepos.invoice

import android.util.Log
import com.skegworks.mobilepos.data.domain.Invoice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncInvoiceUseCaseImpl @Inject constructor(private val repository: InvoiceRepository) :
    SyncInvoiceUseCase {
    override suspend fun invoke(invoice: Invoice) {
        repository.insertInvoice(invoice)
        repository.syncInvoice(
            invoice.apply { isSynced = true },
            onSuccess = { id ->
                Log.d("Firestore", "Added with ID: $id")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.updateInvoice(invoice)
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

        invoice.items.forEach {
            repository.insertInvoiceItem(it)

            repository.syncInvoiceItem(
                it.apply {
                    isSynced = true
                },
                onSuccess = { id ->
                    Log.d("Firestore", "Added with ID: $id")
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.updateInvoice(invoice)
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