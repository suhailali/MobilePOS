package com.skegworks.mobilepos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendors")
data class Vendor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
    @ColumnInfo val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo val updatedAt: Long = System.currentTimeMillis()

)
