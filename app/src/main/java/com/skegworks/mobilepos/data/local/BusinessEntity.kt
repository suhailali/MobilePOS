package com.skegworks.mobilepos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business")
data class BusinessEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "mobile") val mobile: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "address") val address: Boolean,
    @ColumnInfo(name = "gstNumber") var gstNumber: Boolean
)