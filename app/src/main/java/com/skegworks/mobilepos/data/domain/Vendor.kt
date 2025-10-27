package com.skegworks.mobilepos.data.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

data class Vendor(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val phone: String,
    val email: String,
    val zipCode: String,
    val gst: String,
    val gstPercentage: String,
    val currency: String,
    val isActive: Boolean,
    var isSynced: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
