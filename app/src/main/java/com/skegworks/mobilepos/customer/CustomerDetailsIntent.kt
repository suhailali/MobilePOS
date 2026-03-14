package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Customer

sealed class CustomerDetailsIntent {
    data class SetCustomerDetail(val customerId: String) : CustomerDetailsIntent()
    data class CreateNewCoupon(val customer: Customer) : CustomerDetailsIntent()
    object ClearCoupon: CustomerDetailsIntent()

    object Update : CustomerDetailsIntent()
}