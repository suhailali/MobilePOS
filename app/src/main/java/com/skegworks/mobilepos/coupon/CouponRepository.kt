package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon

interface CouponRepository {
    suspend fun getCouponFromCode(barcode: String): List<Coupon>?
}