package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer

data class SearchCustomerState(
    val textStatePhone: String = "",
    val customers:List<Customer> = listOf(),
    val searching: Boolean = false,
    val error:String? = null
)