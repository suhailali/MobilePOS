package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Invoice

interface UpdateCouponPostInvoice {
    suspend operator fun invoke(coupon: Coupon, invoice: Invoice)
}