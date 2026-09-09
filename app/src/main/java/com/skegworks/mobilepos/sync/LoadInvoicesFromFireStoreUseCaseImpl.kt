package com.skegworks.mobilepos.sync

import com.skegworks.mobilepos.business.BusinessRepository
import com.skegworks.mobilepos.coupon.CouponRepository
import com.skegworks.mobilepos.creditnote.CreditNoteRepository
import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.remote.firestore.CreditNoteFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.CreditNoteItemFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.InvoiceFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.InvoiceItemFireStoreDto
import com.skegworks.mobilepos.invoice.InvoiceRepository
import com.skegworks.mobilepos.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.collections.orEmpty

class LoadInvoicesFromFireStoreUseCaseImpl(
    private val syncDataWithFireStore: SyncData,
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CustomerRepository,
    private val businessRepository: BusinessRepository,
    private val couponRepository: CouponRepository,
    private val creditNoteRepository: CreditNoteRepository
) : LoadInvoicesFromFireStoreUseCase {

    private val supervisor = SupervisorJob()
    private val scope = CoroutineScope(supervisor + Dispatchers.IO)

    override operator fun invoke(onCompletion: () -> Unit) {
        scope.launch {
            supervisorScope {
                val tasks = listOf(
                    async {
                        try {
                            getInvoices()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getInvoiceItems()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getCreditNotes()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getCreditNoteItems()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                )
                tasks.awaitAll()
                withContext(Dispatchers.Main) {
                    onCompletion()
                }
            }
        }
    }

    private suspend fun getCreditNoteItems() {
//        val result = syncDataWithFireStore.downloadLatest(
//            Constants.FirebaseDocument.INVOICE_ITEMS,
//            0,
//            InvoiceItemFireStoreDto::class.java
//        )
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.CREDIT_NOTE_ITEMS,
            CreditNoteItemFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (creditNoteItem in result.getOrNull().orEmpty()) {
                creditNoteRepository.insertCreditNoteItem(creditNoteItem.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading credit note items")
        }
    }
    private suspend fun getCreditNotes() {
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.CREDIT_NOTES,
            CreditNoteFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (creditNote in result.getOrNull().orEmpty()) {
                val customer = customerRepository.getCustomerById(creditNote.customerId)
                val business = businessRepository.getBusiness()
                val coupon = couponRepository.getCouponById(creditNote.couponId)

                if (customer != null && business != null) {
                    creditNoteRepository.insertCreditNote(
                        creditNote.toDomain(
                            customer,
                            business,
                            coupon,
                            listOf()
                        )
                    )
                }
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading credit notes")
        }
    }


    private suspend fun getInvoices() {
//        val result = syncDataWithFireStore.downloadLatest(
//            Constants.FirebaseDocument.INVOICES,
//            0,
//            InvoiceFireStoreDto::class.java
//        )
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.INVOICES,
            InvoiceFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (invoice in result.getOrNull().orEmpty()) {
                val customer = customerRepository.getCustomerById(invoice.customerId)
                val business = businessRepository.getBusiness()
                val coupon = couponRepository.getCouponById(invoice.couponId)

                if (customer != null && business != null) {
                    invoiceRepository.insertInvoice(
                        invoice.toDomain(
                            customer,
                            business,
                            coupon,
                            listOf()
                        )
                    )
                }
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading invoices")
        }
    }

    private suspend fun getInvoiceItems() {
//        val result = syncDataWithFireStore.downloadLatest(
//            Constants.FirebaseDocument.INVOICE_ITEMS,
//            0,
//            InvoiceItemFireStoreDto::class.java
//        )
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.INVOICE_ITEMS,
            InvoiceItemFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (invoiceItem in result.getOrNull().orEmpty()) {
                invoiceRepository.insertInvoiceItem(invoiceItem.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading invoice items")
        }
    }
}