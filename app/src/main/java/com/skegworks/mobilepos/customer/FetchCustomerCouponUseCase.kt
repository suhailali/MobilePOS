package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.data.domain.Coupon

interface FetchCustomerCouponUseCase {
    suspend operator fun invoke(customerId: String): List<Coupon>
}