package com.skegworks.mobilepos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "invoice_number") val invoiceNumber: String,
    @ColumnInfo(name = "invoice_date") val invoiceDate: String,
    @ColumnInfo(name = "total_price") val totalPrice: Double,
    @ColumnInfo(name = "total_discount") val totalDiscount: Double,
    @ColumnInfo(name = "final_price") val finalPrice: Double,
    @ColumnInfo(name = "customer_id") val customerId: String,
    @ColumnInfo(name = "customer_name") val customerName: String,
    @ColumnInfo(name = "customer_phone") val customerPhone: String,
    @ColumnInfo(name = "business_id") val businessId: String,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
)