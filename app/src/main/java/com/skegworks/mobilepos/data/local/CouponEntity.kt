package com.skegworks.mobilepos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "discount_given_to_name")
    val discountGivenToName: String,
    @ColumnInfo(name = "discount_given_to_id")
    val discountGivenToId: String,
    @ColumnInfo(name = "discount_given_to_number")
    val discountGivenToNumber: String,
    @ColumnInfo(name = "discount_code")
    val discountCode: String,
    @ColumnInfo(name = "discount_percentage")
    val discountPercentage: Double,
    @ColumnInfo(name = "discount_type")
    val discountType: String,
    @ColumnInfo(name = "discount_valid_till")
    val discountValidTill: Long,
    @ColumnInfo(name = "discount_availed_by")
    val discountAvailedBy: String,
    @ColumnInfo(name = "discount_availed_on")
    val discountAvailedOn: String,
     @ColumnInfo(name = "discounted_amount")
    val discountedAmount: Double,
    @ColumnInfo(name = "invoice_number")
    val invoiceNumber: String,
    @ColumnInfo(name = "invoice_id")
    val invoiceId: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean,
    @ColumnInfo(name = "is_synced")
    var isSynced: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "created_by")
    val createdBy: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "updated_by")
    val updatedBy: String,
)