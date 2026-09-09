package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.business.BusinessDao
import com.skegworks.mobilepos.coupon.CouponDao
import com.skegworks.mobilepos.creditnote.CreditNoteDao
import com.skegworks.mobilepos.customer.CustomerDao
import com.skegworks.mobilepos.data.domain.ChartPoint
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.local.InvoiceEntity
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData
import com.skegworks.mobilepos.utils.DateUtility
import java.time.LocalDate
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val invoiceDao: InvoiceDao,
    private val invoiceItemDao: InvoiceItemDao,
    private val customerDao: CustomerDao,
    private val businessDao: BusinessDao,
    private val couponDao: CouponDao,
    private val syncData: SyncData,
    private val creditNoteDao: CreditNoteDao,
    private val dateUtility: DateUtility
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

    override suspend fun getAllInvoices(): List<Invoice> {
        val creditNoteInvoices = creditNoteDao.getAllInvoiceId().toSet()
        val invoices = invoiceDao.getAllInvoices()
        val newInvoices = invoices.map { invoice ->
            val invoiceItems = invoiceItemDao.getAllInvoiceItems(invoice.id).map {
                it.toDomain()
            }
            val customer = customerDao.getCustomerById(invoice.customerId)
            val business = businessDao.getBusiness()
            val coupon = couponDao.getCouponById(invoice.couponId)?.toDomain()
            if (customer == null || business == null) {
                return@map null
            }
            val invoiceDomain =
                invoice.toDomain(customer.toDomain(), business.toDomain(), coupon, invoiceItems)
            if (creditNoteInvoices.contains(invoiceDomain.id)) {
                invoiceDomain.isCreditNote = true
            }
            invoiceDomain
        }
        if (newInvoices.isEmpty()) {
            return emptyList()
        }
        return newInvoices.filterNotNull()
    }

    override suspend fun getPagedInvoices(limit: Int, offset: Int, searchValue: String): List<Invoice> {
        val creditNoteInvoices = creditNoteDao.getAllInvoiceId().toSet()
        val invoices = invoiceDao.getPagedInvoices(limit, offset, searchValue)
        val newInvoices = invoices.map { invoice ->
            val invoiceItems = invoiceItemDao.getAllInvoiceItems(invoice.id).map {
                it.toDomain()
            }
            val customer = customerDao.getCustomerById(invoice.customerId)
            val business = businessDao.getBusiness()
            val coupon = couponDao.getCouponById(invoice.couponId)?.toDomain()
            if (customer == null || business == null) {
                return@map null
            }
            val invoiceDomain =
                invoice.toDomain(customer.toDomain(), business.toDomain(), coupon, invoiceItems)
            if (creditNoteInvoices.contains(invoiceDomain.id)) {
                invoiceDomain.isCreditNote = true
            }
            invoiceDomain
        }
        return newInvoices.filterNotNull()
    }

    override suspend fun getInvoicesByCustomer(customerId: String): List<Invoice> {
        val invoices = invoiceDao.getInvoicesByCustomer(customerId)
        val newInvoices = invoices.map { invoice ->
            val invoiceItems = invoiceItemDao.getAllInvoiceItems(invoice.id).map {
                it.toDomain()
            }
            val customer = customerDao.getCustomerById(invoice.customerId)
            val business = businessDao.getBusiness()
            val coupon = couponDao.getCouponById(invoice.couponId)?.toDomain()
            if (customer == null || business == null) {
                return@map null
            }
            invoice.toDomain(customer.toDomain(), business.toDomain(), coupon, invoiceItems)
        }
        if (newInvoices.isEmpty()) {
            return emptyList()
        }
        return newInvoices.filterNotNull()
    }

    override suspend fun getUnsyncedInvoices(): List<Invoice> {
        val invoices = invoiceDao.getUnsyncedInvoices()
        return getInvoiceDetails(invoices)
    }

    override suspend fun getInvoicesAmountByDate(): List<ChartPoint> {
        val invoiceAmountByDate = invoiceDao.getInvoiceAmountByDay().take(7)
        val chartPoints = invoiceAmountByDate.map { invoiceAmount ->
            ChartPoint(invoiceAmount.invoiceNumber, invoiceAmount.amount)
        }
        return chartPoints
    }

    override suspend fun getInvoiceById(id: String): Invoice? {
        val invoice = invoiceDao.getInvoiceById(id) ?: return null
        val invoiceItems = invoiceItemDao.getAllInvoiceItems(id).map {
            it.toDomain()
        }
        val customer = customerDao.getCustomerById(invoice.customerId)
        val business = businessDao.getBusiness()
        val coupon = couponDao.getCouponById(invoice.couponId)?.toDomain()
        if (customer == null || business == null) {
            return null
        }
        val invoiceDomain =
            invoice.toDomain(customer.toDomain(), business.toDomain(), coupon, invoiceItems)
        return invoiceDomain
    }

    override suspend fun getInvoiceForLastSevenDays(): List<Invoice> {
        val invoices = invoiceDao.getInvoicesForAPeriod(
            dateUtility.daysBeforeInMillis(
                7,
                LocalDate.now()
            )
        )
        return getInvoiceDetails(invoices)
    }

    override suspend fun getInvoiceForToday(): List<Invoice> {
        val invoices = invoiceDao.getInvoicesForAPeriod(
            dateUtility.daysBeforeInMillis(
                0,
                LocalDate.now()
            )
        )
        return getInvoiceDetails(invoices)
    }

    override suspend fun getInvoiceForThisMonth(): List<Invoice> {
        val dayOfMonth = LocalDate.now().dayOfMonth
        val invoices = invoiceDao.getInvoicesForAPeriod(
            dateUtility.daysBeforeInMillis(
                dayOfMonth.toLong() - 1,
                LocalDate.now()
            )
        )
        return getInvoiceDetails(invoices)
    }

    suspend fun getInvoiceDetails(invoices: List<InvoiceEntity>): List<Invoice> {
        val newInvoices = invoices.map { invoice ->
            val invoiceItems = invoiceItemDao.getAllInvoiceItems(invoice.id).map {
                it.toDomain()
            }
            val customer = customerDao.getCustomerById(invoice.customerId)
            val business = businessDao.getBusiness()
            val coupon = couponDao.getCouponById(invoice.couponId)?.toDomain()
            if (customer == null || business == null) {
                return@map null
            }
            invoice.toDomain(customer.toDomain(), business.toDomain(), coupon, invoiceItems)
        }
        if (newInvoices.isEmpty()) {
            return emptyList()
        }
        return newInvoices.filterNotNull()
    }
}
