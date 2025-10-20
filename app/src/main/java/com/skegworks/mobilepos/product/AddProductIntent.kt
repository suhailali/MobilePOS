package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.vendors.VendorDetailsIntent

sealed class AddProductIntent {
    data class UpdateVendor(val vendor: String): AddProductIntent()
    data class UpdateHsnCode(val hsnCode: String): AddProductIntent()
    data class UpdateTitle(val title: String): AddProductIntent()
    data class UpdateCategory(val category: String) : AddProductIntent()
    data class UpdateSku(val sku: String) : AddProductIntent()
    data class UpdateSize(val size: String): AddProductIntent()
    data class UpdateColor(val color: String) : AddProductIntent()
    data class UpdateCost(val cost: Double) : AddProductIntent()
    data class UpdateSalePrice(val salePrice: Double) : AddProductIntent()
    data class UpdateQuantity(val quantity: Int) : AddProductIntent()
    data class UpdateAlertQuantity(val alertQuantity: Int): AddProductIntent()
    data class UpdateDescription(val description: String): AddProductIntent()
    data class UpdateImageUrl(val imageUrl: String) : AddProductIntent()
    data class UpdateGstPercentage(val gstPercentage: Double) : AddProductIntent()
    data class UpdateIsActive(val isActive: Boolean) : AddProductIntent()
    data class UpdateDiscountPercentage(val discountPercentage: Double) : AddProductIntent()
    data class UpdateCreatedAt(val createdAt: Long) : AddProductIntent()
    data class UpdateUpdatedAt(val updatedAt: Long) : AddProductIntent()
    object Save : AddProductIntent()
}