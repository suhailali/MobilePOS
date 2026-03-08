package com.skegworks.mobilepos.coupon

import android.graphics.Bitmap


interface GenerateCouponImageUseCase {
    suspend operator fun invoke(
        inputPath: String,
        couponCode: String,
        customerName: String,
        discount: String,
        validFrom: String,
        validTill: String
    ): Bitmap?
}