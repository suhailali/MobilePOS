package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.InvoiceItem

interface UpdateInventoryAfterCreditNoteUseCase {
    suspend operator fun invoke(items: List<InvoiceItem>)
}