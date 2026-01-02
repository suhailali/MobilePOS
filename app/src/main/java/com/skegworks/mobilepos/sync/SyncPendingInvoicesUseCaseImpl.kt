package com.skegworks.mobilepos.sync

import android.util.Log
import com.skegworks.mobilepos.invoice.InvoiceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncPendingInvoicesUseCaseImpl @Inject constructor(private val repository: InvoiceRepository) :
    SyncPendingInvoicesUseCase {
    override suspend fun invoke() {
        val invoices = repository.getUnsyncedInvoices()
        invoices.forEach { invoice ->
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
}