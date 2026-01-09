package com.skegworks.mobilepos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "creditNote")
data class CreditNoteEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "invoice_id") val invoiceId: String,
    @ColumnInfo(name = "invoice_number") val invoiceNumber: String,
    @ColumnInfo(name = "invoice_date") val invoiceDate: String,
    @ColumnInfo(name = "total_price") val totalPrice: Double,
    @ColumnInfo(name = "total_discount") val totalDiscount: Double,
    @ColumnInfo(name = "final_price") val finalPrice: Double,
    @ColumnInfo(name = "customer_id") val customerId: String,
    @ColumnInfo(name = "customer_name") val customerName: String,
    @ColumnInfo(name = "customer_phone") val customerPhone: String,
    @ColumnInfo(name = "business_id") val businessId: String,
    @ColumnInfo(name = "coupon_id") val couponId: String,
    @ColumnInfo(name = "cash_discount") val cashDiscount: Double,
    @ColumnInfo(name = "coupon_discount") val couponDiscount: Double,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
    @ColumnInfo(name = "credit_note_date") val creditNoteDate: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "updated_at") var updatedAt: Long,
)