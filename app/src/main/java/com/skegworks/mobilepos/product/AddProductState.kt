package com.skegworks.mobilepos.product

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.skegworks.mobilepos.data.domain.Category
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.domain.Vendor

data class AddProductState(
    val textStateCategoryId: String = "",
    val textStateCategoryName: String = "",

    val textStateVendorId: String = "",
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
    val textStateFinalRoundedOffPrice: Int = 0,

    val textStateHsnCode: String = "",
    val textStateQuantity: Int = 0,
    val textStateAlertQuantity: Int = 0,
    val textStateDescription: String = "",
    val textStateImageUrl: String = "",
    val textStateIsActive: Boolean = true,
    val textStateDiscountPercentage: Double = 0.0,
    val textStateDiscountAmount: Double = 0.0,
    val textStatePriceAfterDiscountWithoutGst: Double = 0.0,
    val textStateCreatedAt: Long = System.currentTimeMillis(),
    val textStateUpdatedAt: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false,
    val categories: List<Category> = emptyList(),
    val vendors: List<Vendor> = emptyList(),
    val gstPercentages: List<Double> = listOf(0.0, 1.0, 2.5, 5.0, 6.0, 12.0, 18.0, 28.0),
    val discountPercentages: List<Double> = listOf(0.0, 1.0, 2.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0,12.0, 15.0,18.0, 20.0,25.0,28.0, 30.0, 35.0, 40.0, 45.0, 50.0, 55.0, 60.0),
    val saleMargins: List<Int> = listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 43, 45, 48, 50, 53, 55, 58, 60, 65, 70, 75, 80, 85, 90, 95, 100),
    val isLoading: Boolean = false,
    val barcodeBitmap: Bitmap? = null,
    val color: List<Color> = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta,
        Color.Cyan, Color.Gray, Color.Black, Color.White, Color.LightGray, Color.DarkGray, Color.Transparent, Color.Unspecified),
    val size: List<String> = listOf("S", "M", "L", "XL", "XXL", "XXXL", "Free", "NONE"),
    val quantity: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50),

    val products: List<Product> = emptyList(),

    val errorSku:Boolean = false,
    val validationErrorMessageSku:String = "",
    val errorCalculatePrice: Boolean = false,
    val validationErrorMessageCalculatePrice:String = "",
    val errorSave:Boolean = false,
    val validationErrorMessageSave:String = ""
) {
    fun clearState(): AddProductState {
        return AddProductState(
            categories = this.categories,
            vendors = this.vendors
        )
    }
}
