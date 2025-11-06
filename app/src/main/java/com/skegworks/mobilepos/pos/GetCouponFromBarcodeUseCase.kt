package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Coupon

interface GetCouponFromBarcodeUseCase {
    suspend operator fun invoke(barcode: String): List<Coupon>?
}