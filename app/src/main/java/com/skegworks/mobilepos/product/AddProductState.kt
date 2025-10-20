package com.skegworks.mobilepos.product

import android.graphics.Bitmap
import com.skegworks.mobilepos.data.Category
import com.skegworks.mobilepos.data.Vendor

data class AddProductState(
    val textStateCategoryId: Int = -1,
    val textStateCategoryName: String = "",

    val textStateVendorId: Int = -1,
    val textStateVendorName: String = "",

    val textStateTitle: String = "",

    val textStateSize: String = "",
    val textStateColor: String = "",
    val textStateSku: String = "",


    val textStateItemPrice: Double = 0.0,
    val textStateInputGstPercentage: Double = 0.0,
    val textStateOutputGstPercentage: Double = 0.0,
    val textStateSaleMargin: Int = 0,
    val textStateInputGst: Double = 0.0,
    val textStateCost: Double = 0.0,
    val textStateSalePriceWithoutGst: Double = 0.0,
    val textStateOutputGst: Double = 0.0,
    val textStateSalePrice: Double = 0.0,

    val textStateHsnCode: String = "",
    val textStateQuantity: Int = 0,
    val textStateAlertQuantity: Int = 0,
    val textStateDescription: String = "",
    val textStateImageUrl: String = "",
    val textStateIsActive: Boolean = true,
    val textStateDiscountPercentage: Double = 0.0,
    val textStateCreatedAt: Long = System.currentTimeMillis(),
    val textStateUpdatedAt: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false,
    val categories: List<Category> = emptyList(),
    val vendors: List<Vendor> = emptyList(),
    val gstPercentages: List<Double> = listOf(0.0, 1.0, 2.5, 5.0, 6.0, 18.0, 28.0),
    val saleMargins: List<Int> = listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 43, 45, 48, 50, 53, 55, 58, 60, 65, 70, 75, 80, 85, 90, 95, 100),
    val isLoading: Boolean = false,
    val barcodeBitmap: Bitmap? = null,
) {
    fun clearState(): AddProductState {
        return AddProductState(
            categories = this.categories,
            vendors = this.vendors
        )
    }
}
