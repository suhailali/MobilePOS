package com.skegworks.mobilepos.business

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.BusinessEntity

@Dao
interface BusinessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: BusinessEntity)

    @Query("SELECT * FROM business WHERE id = :id")
    suspend fun getBusinessById(id: String): BusinessEntity?

    @Query("SELECT * FROM business")
    suspend fun getAllBusiness(): List<BusinessEntity>

    @Update
    suspend fun updateBusiness(business: BusinessEntity)

    @Delete
    suspend fun deleteBusiness(business: BusinessEntity)
}