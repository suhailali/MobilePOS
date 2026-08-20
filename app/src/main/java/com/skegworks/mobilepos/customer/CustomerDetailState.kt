package com.skegworks.mobilepos.customer

import android.graphics.Bitmap
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.domain.Invoice

data class CustomerDetailState(
    val customer: Customer? = null,
    val customerId: String = "",
    val invoiceList: List<Invoice> = emptyList(),
    val couponList: List<Coupon> = emptyList(),
    val generatedCoupon: Bitmap? = null,
    val isLastPage: Boolean = false,
    val currentPage: Int = 0
)
