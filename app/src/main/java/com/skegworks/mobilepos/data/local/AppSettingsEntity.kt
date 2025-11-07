package com.skegworks.mobilepos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "invoice_counter")
    var invoiceCounter: Long,

    @ColumnInfo(name = "invoice_year")
    var invoiceYear: Int,

    @ColumnInfo(name = "product_sku_counter")
    var productSkuCounter: Long,

    @ColumnInfo(name = "is_synced")
    var isSynced: Boolean,

    @ColumnInfo(name = "created_at")
    var createdAt: Long,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long,

    @ColumnInfo(name = "updated_by")
    var updatedBy: String

)