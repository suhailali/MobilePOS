package com.skegworks.mobilepos.customer

sealed class AddCustomerIntent {
    data class UpdateName(val name: String): AddCustomerIntent()
    data class UpdateAddress(val address: String) : AddCustomerIntent()
    data class UpdatePhone(val phone: String) : AddCustomerIntent()
    data class UpdateEmail(val email: String) : AddCustomerIntent()
    data class UpdateCreatedAt(val createdAt: Long) : AddCustomerIntent()
    data class UpdateUpdatedAt(val updatedAt: Long) : AddCustomerIntent()

    object Save : AddCustomerIntent()
}