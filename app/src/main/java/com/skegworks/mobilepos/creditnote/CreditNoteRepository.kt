package com.skegworks.mobilepos.creditnote

import com.skegworks.mobilepos.data.domain.CreditNote
import com.skegworks.mobilepos.data.domain.CreditNoteItem

interface CreditNoteRepository {
    suspend fun insertCreditNote(creditNote: CreditNote)
    suspend fun insertCreditNoteItem(creditNoteItem: CreditNoteItem)

    suspend fun updateCreditNote(creditNote: CreditNote)

    suspend fun updateCreditNoteItem(creditNoteItem: CreditNoteItem)
    suspend fun getCreditNoteItemByInvoiceItemId(invoiceItemId: String): CreditNoteItem?
    suspend fun getCreditNoteItemByVendorId(vendorId: String): CreditNoteItem?
    suspend fun isInvoiceAddedToCreditNote(invoiceId: String): Boolean
    suspend fun syncCreditNote(creditNote: CreditNote,  onSuccess: (String) -> Unit,
                               onFailure: (Exception) -> Unit)
    suspend fun syncCreditNoteItem(creditNoteItem: CreditNoteItem,  onSuccess: (String) -> Unit,
                                   onFailure: (Exception) -> Unit)
}