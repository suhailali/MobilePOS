package com.skegworks.mobilepos.vendors

sealed class VendorDetailsIntent {
    data class UpdateName(val value: String): VendorDetailsIntent()
    data class UpdateAddress(val value: String): VendorDetailsIntent()
    data class UpdateCity(val value: String): VendorDetailsIntent()
    data class UpdateState(val value: String): VendorDetailsIntent()
    data class UpdateCountry(val value: String): VendorDetailsIntent()
    data class UpdatePhone(val value: String): VendorDetailsIntent()
    data class UpdateEmail(val value: String): VendorDetailsIntent()
    data class UpdateZipCode(val value: String): VendorDetailsIntent()
    data class UpdateGST(val value: String): VendorDetailsIntent()
    data class UpdateGSTPercentage(val value: String): VendorDetailsIntent()
    data class UpdateCurrency(val value: String): VendorDetailsIntent()
    object Save : VendorDetailsIntent()
}