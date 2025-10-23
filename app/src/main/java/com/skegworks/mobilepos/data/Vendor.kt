package com.skegworks.mobilepos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendors")
data class Vendor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val address: String,
    @ColumnInfo val city: String,
    @ColumnInfo val state: String,
    @ColumnInfo val country: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val email: String,
    @ColumnInfo val zipCode: String,
    @ColumnInfo val gst: String,
    @ColumnInfo val gstPercentage: String,
    @ColumnInfo val currency: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean = false,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false,
    @ColumnInfo val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo val updatedAt: Long = System.currentTimeMillis()

)
