package com.skegworks.mobilepos.creditnote
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.CreditNoteEntity

@Dao
interface CreditNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditNote(creditNote: CreditNoteEntity)

    @Query("SELECT * FROM creditNote WHERE id = :id")
    suspend fun getCreditNoteById(id: String): CreditNoteEntity?

    @Query("SELECT * FROM creditNote WHERE invoice_id = :id")
    suspend fun getCreditNoteByInvoiceId(id: String): CreditNoteEntity?

    @Query("SELECT * FROM creditNote ORDER BY credit_note_date DESC")
    suspend fun getAllCreditNotes(): List<CreditNoteEntity>

    @Update
    suspend fun updateCreditNote(creditNote: CreditNoteEntity)

    @Delete
    suspend fun deleteCreditNote(creditNote: CreditNoteEntity)
}
