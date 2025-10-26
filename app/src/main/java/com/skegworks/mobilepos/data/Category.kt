package com.skegworks.mobilepos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.UUID

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),
)
