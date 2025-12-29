package com.skegworks.mobilepos.invoice

import android.util.Log
import com.skegworks.mobilepos.creditnote.CreditNoteRepository
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.mapper.toCreditNote
import com.skegworks.mobilepos.data.mapper.toCreditNoteItem
import com.skegworks.mobilepos.utils.DateUtility
import com.skegworks.mobilepos.utils.UUIDGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GenerateNewCreditNoteUseCaseImpl @Inject constructor(
    private val dateUtility: DateUtility,
    private val uuidGenerator: UUIDGenerator,
    private val creditNoteRepository: CreditNoteRepository
) : GenerateNewCreditNoteUseCase {

    override suspend fun invoke(
        invoice: Invoice,
        invoiceItems: List<InvoiceItem>,
        returnDescription: String
    ) {
        val creditNoteId = uuidGenerator.generateUUID()
        val creditNoteItems = invoiceItems.map {
            it.toCreditNoteItem(creditNoteId, uuidGenerator.generateUUID())
        }
        val creditNote = invoice.toCreditNote(creditNoteId, creditNoteItems)
        creditNote.creditNoteDate = dateUtility.getDateTime()
        creditNote.description = returnDescription
        creditNoteRepository.insertCreditNote(creditNote)
        creditNoteRepository.syncCreditNote(
            creditNote.apply { isSynced = true },
            onSuccess = { id ->
                Log.d("Firestore", "Added with ID: $id")
                CoroutineScope(Dispatchers.IO).launch {
                    creditNoteRepository.updateCreditNote(creditNote)
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
        creditNoteItems.forEach {
            creditNoteRepository.syncCreditNoteItem(
                it.apply { isSynced = true },
                onSuccess = { id ->
                    Log.d("Firestore", "Added with ID: $id")
                    CoroutineScope(Dispatchers.IO).launch {
                        creditNoteRepository.updateCreditNoteItem(it)
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