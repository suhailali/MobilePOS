package com.skegworks.mobilepos.customer

sealed class SearchCustomerIntent {
    data class SearchPhone(val phone: String) : SearchCustomerIntent()
}