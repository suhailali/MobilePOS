package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon

interface CouponRepository {
    suspend fun getCouponFromCode(barcode: String): List<Coupon>?
    suspend fun insertCoupon(coupon: Coupon)
    suspend fun getCouponById(id: String): Coupon?
    suspend fun getAllCoupons(): List<Coupon>
    suspend fun updateCoupon(coupon: Coupon)
    suspend fun deleteCoupon(coupon: Coupon)
    suspend fun deleteAllCoupons()
    suspend fun syncCoupon(
        coupon: Coupon,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun syncAllCoupons(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit)
}