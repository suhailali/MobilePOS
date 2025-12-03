package com.skegworks.mobilepos.invoice
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.InvoiceItemEntity

@Dao
interface InvoiceItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoiceItem(invoiceItem: InvoiceItemEntity)

    @Query("SELECT * FROM invoice_items WHERE id = :id")
    suspend fun getInvoiceItemById(id: String): InvoiceItemEntity?

    @Query("SELECT * FROM invoice_items WHERE invoice_id = :invoiceId")
    suspend fun getAllInvoiceItems(invoiceId: String): List<InvoiceItemEntity>

    @Update
    suspend fun updateInvoiceItem(invoiceItem: InvoiceItemEntity)

    @Delete
    suspend fun deleteInvoiceItem(invoiceItem: InvoiceItemEntity)
}
