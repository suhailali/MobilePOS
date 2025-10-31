package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val invoiceDao: InvoiceDao,
    private val invoiceItemDao: InvoiceItemDao,
    private val syncData: SyncData
) :
    InvoiceRepository {
    override suspend fun insertInvoice(invoice: Invoice) {
        invoiceDao.insertInvoice(invoice.toEntity())
    }

    override suspend fun updateInvoice(invoice: Invoice) {
        invoiceDao.updateInvoice(invoice.toEntity())
    }

    override suspend fun syncInvoice(
        invoice: Invoice,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = "invoices",
            id = invoice.id,
            data = invoice.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun insertInvoiceItem(invoiceItem: InvoiceItem) {
        invoiceItemDao.insertInvoiceItem(invoiceItem.toEntity())
    }

    override suspend fun updateInvoiceItem(invoiceItem: InvoiceItem) {
        invoiceItemDao.updateInvoiceItem(invoiceItem.toEntity())
    }

    override suspend fun syncInvoiceItem(
        invoiceItem: InvoiceItem,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = "invoices_items",
            id = invoiceItem.id,
            data = invoiceItem.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}