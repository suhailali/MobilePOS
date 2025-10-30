package com.skegworks.mobilepos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.UUID

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "vendor_id") val vendorId: String,
    @ColumnInfo(name = "vendor_Name") val vendorName: String,
    @ColumnInfo(name = "hsn_code") val hsnCode: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "category_id") val categoryId: String,
    @ColumnInfo(name = "category_Name") val categoryName: String,
    @ColumnInfo(name = "sku") val sku: String,
    @ColumnInfo(name = "size") val size: String,
    @ColumnInfo(name = "color") val color: String,

    @ColumnInfo(name = "item_price") val itemPrice: Double,
    @ColumnInfo(name = "input_gst_percentage") val inputGstPercentage: Double,
    @ColumnInfo(name = "input_gst") val inputGst: Double,
    @ColumnInfo(name = "output_gst_percentage") val outputGstPercentage: Double,
    @ColumnInfo(name = "output_gst") val outputGst: Double,
    @ColumnInfo(name = "sale_margin") val saleMargin: Int,
    @ColumnInfo(name = "cost") val cost: Double,
    @ColumnInfo(name = "sale_price_without_gst") val salePriceWithoutGst: Double,
    @ColumnInfo(name = "sale_price") val salePrice: Double,
    @ColumnInfo(name = "final_rounded_off_price") val finalRoundedOffPrice: Int,

    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "alert_quantity") val alertQuantity: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "discount_percentage") val discountPercentage: Double,
    @ColumnInfo(name = "discount_amount") val discountAmount: Double,
    @ColumnInfo(name = "created_by") val createdBy: String,
    @ColumnInfo(name = "updated_by") val updatedBy: String,
)
