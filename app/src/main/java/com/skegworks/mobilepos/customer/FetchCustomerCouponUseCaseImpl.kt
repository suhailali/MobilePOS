package com.skegworks.mobilepos.customer

import com.skegworks.mobilepos.coupon.CouponRepository
import com.skegworks.mobilepos.data.domain.Coupon
import javax.inject.Inject

class FetchCustomerCouponUseCaseImpl @Inject constructor(private val repository: CouponRepository): FetchCustomerCouponUseCase {
    override suspend fun invoke(customerId: String): List<Coupon> {
        return repository.getCouponsByCustomer(customerId)
    }
}