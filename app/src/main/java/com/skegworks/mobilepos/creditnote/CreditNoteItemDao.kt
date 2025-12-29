package com.skegworks.mobilepos.creditnote
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.CreditNoteItemEntity

@Dao
interface CreditNoteItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditNoteItem(creditNoteItem: CreditNoteItemEntity)

    @Query("SELECT * FROM credit_note_items WHERE id = :id")
    suspend fun getCreditNoteItemById(id: String): CreditNoteItemEntity?

    @Query("SELECT * FROM credit_note_items WHERE invoice_item_id = :id")
    suspend fun getCreditNoteItemByInvoiceItemId(id: String): CreditNoteItemEntity?

    @Query("SELECT * FROM credit_note_items WHERE credit_note_id = :creditNoteId")
    suspend fun getAllCreditNoteItems(creditNoteId: String): List<CreditNoteItemEntity>

    @Update
    suspend fun updateCreditNoteItem(creditNoteItem: CreditNoteItemEntity)

    @Delete
    suspend fun deleteCreditNoteItem(creditNoteItem: CreditNoteItemEntity)
}
