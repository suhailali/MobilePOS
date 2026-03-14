package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon

interface UploadCouponForCustomerUseCase {
    suspend operator fun invoke(coupon: Coupon)
}