package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.domain.Category
import com.skegworks.mobilepos.data.domain.Vendor

sealed class AddProductIntent {
    data class UpdateVendor(val vendor: Vendor): AddProductIntent()
    data class UpdateHsnCode(val hsnCode: String): AddProductIntent()
    data class UpdateTitle(val title: String): AddProductIntent()
    data class UpdateCategory(val category: Category) : AddProductIntent()
    data class UpdateSku(val sku: String) : AddProductIntent()
    data class UpdateSize(val size: String): AddProductIntent()
    data class UpdateColor(val color: String) : AddProductIntent()



    data class UpdateItemPrice(val itemPrice: Double) : AddProductIntent()
    data class UpdateInputGstPercentage(val inputGstPercentage: Double) : AddProductIntent()
    data class UpdateOutputGstPercentage(val outputGstPercentage: Double) : AddProductIntent()
    data class UpdateSaleMargin(val saleMargin: Int) : AddProductIntent()
    data object CalculatePricing : AddProductIntent()

    data class UpdateQuantity(val quantity: Int) : AddProductIntent()
    data class UpdateAlertQuantity(val alertQuantity: Int): AddProductIntent()
    data class UpdateDescription(val description: String): AddProductIntent()
    data class UpdateImageUrl(val imageUrl: String) : AddProductIntent()
    data class UpdateIsActive(val isActive: Boolean) : AddProductIntent()
    data class UpdateDiscountPercentage(val discountPercentage: Double) : AddProductIntent()
    data class UpdateCreatedAt(val createdAt: Long) : AddProductIntent()
    data class UpdateUpdatedAt(val updatedAt: Long) : AddProductIntent()
    object Save : AddProductIntent()
    object AddAnother : AddProductIntent()

    data object LoadCategories : AddProductIntent()
    data object LoadVendors : AddProductIntent()
    data object GenerateSku : AddProductIntent()
    data object NavigateToAddProduct : AddProductIntent()
}