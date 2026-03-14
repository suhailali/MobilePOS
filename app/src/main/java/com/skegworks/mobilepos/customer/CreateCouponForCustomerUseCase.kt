package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Customer

interface CreateCouponForCustomerUseCase {
    suspend operator fun invoke(customer: Customer, coupon: Coupon) {

    }
}