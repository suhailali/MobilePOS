package com.skegworks.mobilepos.creditnote

import com.skegworks.mobilepos.data.domain.CreditNote
import com.skegworks.mobilepos.data.domain.CreditNoteItem
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData
import com.skegworks.mobilepos.utils.Constants.FirebaseDocument.Companion.CREDIT_NOTES
import com.skegworks.mobilepos.utils.Constants.FirebaseDocument.Companion.CREDIT_NOTE_ITEMS
import javax.inject.Inject

class CreditNoteRepositoryImpl @Inject constructor (
    private val creditNoteDao: CreditNoteDao,
    private val creditNoteItemDao: CreditNoteItemDao,
    private val syncData: SyncData
): CreditNoteRepository {
    override suspend fun insertCreditNote(creditNote: CreditNote) {
        creditNoteDao.insertCreditNote(creditNote.toEntity())
        creditNote.items.forEach {
            insertCreditNoteItem(it)
        }
    }

    override suspend fun insertCreditNoteItem(creditNoteItem: CreditNoteItem) {
        creditNoteItemDao.insertCreditNoteItem(creditNoteItem.toEntity())
    }

    override suspend fun updateCreditNote(creditNote: CreditNote) {
        creditNoteDao.updateCreditNote(creditNote.toEntity())
    }

    override suspend fun updateCreditNoteItem(creditNoteItem: CreditNoteItem) {
        creditNoteItemDao.updateCreditNoteItem(creditNoteItem.toEntity())
    }

    override suspend fun getCreditNoteItemByInvoiceItemId(invoiceItemId: String): CreditNoteItem? {
        return creditNoteItemDao.getCreditNoteItemByInvoiceItemId(invoiceItemId)?.toDomain()
    }

    override suspend fun getCreditNoteItemByVendorId(vendorId: String): CreditNoteItem? {
        return creditNoteItemDao.getCreditNoteItemByVendorId(vendorId)?.toDomain()
    }

    override suspend fun isInvoiceAddedToCreditNote(invoiceId: String): Boolean {
        return creditNoteDao.getCreditNoteByInvoiceId(invoiceId) != null
    }

    override suspend fun syncCreditNote(
        creditNote: CreditNote,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = CREDIT_NOTES,
            id = creditNote.id,
            data = creditNote.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncCreditNoteItem(
        creditNoteItem: CreditNoteItem,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = CREDIT_NOTE_ITEMS,
            id = creditNoteItem.id,
            data = creditNoteItem.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}