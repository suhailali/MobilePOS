package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Customer

class AddNewCouponForCustomerUseCaseImpl: AddNewCouponForCustomerUseCase {
    override suspend fun invoke(
        customer: Customer,
        coupon: Coupon
    ): Boolean {
        TODO("Not yet implemented")
    }
}