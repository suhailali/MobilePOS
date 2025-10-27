package com.skegworks.mobilepos.vendors

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.domain.Vendor
import com.skegworks.mobilepos.data.local.VendorEntity

@Dao
interface VendorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVendor(vendor: VendorEntity)

    @Query("SELECT * FROM vendors WHERE id = :id")
    suspend fun getVendorById(id: String): VendorEntity?

    @Query("SELECT * FROM vendors")
    suspend fun getAllVendors(): List<VendorEntity>

    @Update
    suspend fun updateVendor(vendor: VendorEntity)

    @Delete
    suspend fun deleteVendor(vendor: VendorEntity)
}