package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.mapper.toDomain
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(private val couponDao: CouponDao): CouponRepository {
    override suspend fun getCouponFromCode(barcode: String): List<Coupon>? {
        return couponDao.getCouponByCode(barcode)?.map {
            it.toDomain()
        }
    }
}