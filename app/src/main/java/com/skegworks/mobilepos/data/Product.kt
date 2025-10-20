package com.skegworks.mobilepos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "vendor_id") val vendorId: String,
    @ColumnInfo(name = "vendor_Name") val vendorName: String,
    @ColumnInfo(name = "hsn_code") val hsnCode: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "category_id") val categoryId: String,
    @ColumnInfo(name = "category_Name") val categoryName: String,
    @ColumnInfo(name = "sku") val sku: String,
    @ColumnInfo(name = "size") val size: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "cost") val cost: Double,
    @ColumnInfo(name = "sale_price") val salePrice: Double,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "alert_quantity") val alertQuantity: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "gst_percentage") val gstPercentage: Double,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "discount_percentage") val discountPercentage: Double = 0.0,
)
