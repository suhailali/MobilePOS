package com.skegworks.mobilepos.data.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "vendors")
data class Vendor(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo val name: String,
    @ColumnInfo val address: String,
    @ColumnInfo val city: String,
    @ColumnInfo val state: String,
    @ColumnInfo val country: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val email: String,
    @ColumnInfo(name = "zip_code") val zipCode: String,
    @ColumnInfo val gst: String,
    @ColumnInfo(name = "gst_percentage") val gstPercentage: String,
    @ColumnInfo val currency: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean = false,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()

)
