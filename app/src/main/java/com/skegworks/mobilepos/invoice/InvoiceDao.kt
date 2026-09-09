package com.skegworks.mobilepos.invoice

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.DayInvoiceAmount
import com.skegworks.mobilepos.data.local.InvoiceEntity

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: InvoiceEntity)

    @Query("SELECT * FROM invoices WHERE id = :id")
    suspend fun getInvoiceById(id: String): InvoiceEntity?

    @Query("SELECT * FROM invoices ORDER BY updated_at DESC, invoice_number DESC")
    suspend fun getAllInvoices(): List<InvoiceEntity>

    @Query("SELECT * FROM invoices WHERE customer_phone LIKE '%' || :searchValue || '%' OR customer_name LIKE '%' || :searchValue || '%' ORDER BY updated_at DESC, invoice_number DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedInvoices(limit: Int, offset: Int, searchValue: String): List<InvoiceEntity>

    @Query("SELECT * FROM invoices WHERE is_synced = 0 ORDER BY invoice_number DESC")
    suspend fun getUnsyncedInvoices(): List<InvoiceEntity>

    @Query("SELECT invoice_date as invoiceNumber, SUM(final_price) as amount FROM invoices GROUP BY invoice_date ORDER BY invoice_date DESC")
    suspend fun getInvoiceAmountByDay(): List<DayInvoiceAmount>

    @Update
    suspend fun updateInvoice(invoice: InvoiceEntity)

    @Delete
    suspend fun deleteInvoice(invoice: InvoiceEntity)

    @Query("SELECT * FROM invoices WHERE customer_id = :customerId ORDER BY invoice_number DESC")
    fun getInvoicesByCustomer(customerId: String): List<InvoiceEntity>

    @Query("SELECT * FROM invoices WHERE updated_at > :time ORDER BY invoice_date DESC")
    fun getInvoicesForAPeriod(time: Long): List<InvoiceEntity>
}
