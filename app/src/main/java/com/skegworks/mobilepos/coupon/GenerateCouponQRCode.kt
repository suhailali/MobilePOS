package com.skegworks.mobilepos.coupon

import android.graphics.Bitmap

interface GenerateCouponQRCode {
    suspend fun generateQR(size: Int, data: String): Bitmap
}