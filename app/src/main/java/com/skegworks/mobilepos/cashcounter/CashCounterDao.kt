package com.skegworks.mobilepos.cashcounter

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.CashCounterEntity

@Dao
interface CashCounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashCounter(cashCounter: CashCounterEntity)

    @Query("SELECT * FROM cash_counter WHERE id = :id")
    suspend fun getCashCounterById(id: String): CashCounterEntity?

    @Query("SELECT * FROM cash_counter")
    suspend fun getAllCashCounters(): List<CashCounterEntity>

    @Update
    suspend fun updateCashCounter(cashCounter: CashCounterEntity)

    @Delete
    suspend fun deleteCashCounter(cashCounter: CashCounterEntity)

    @Query("SELECT * FROM cash_counter ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestCashCounter(): CashCounterEntity?
}