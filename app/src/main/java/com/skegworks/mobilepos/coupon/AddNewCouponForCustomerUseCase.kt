package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Customer

interface AddNewCouponForCustomerUseCase {
    suspend operator fun invoke(customer: Customer, coupon: Coupon): Boolean
}