package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.coupon.CouponRepository
import com.skegworks.mobilepos.data.domain.Coupon

class GetCouponFromBarCodeUseCaseImpl(private val couponRepository: CouponRepository): GetCouponFromBarcodeUseCase {
    override suspend fun invoke(barcode: String): List<Coupon>? {
        return couponRepository.getCouponFromCode(barcode)
    }
}