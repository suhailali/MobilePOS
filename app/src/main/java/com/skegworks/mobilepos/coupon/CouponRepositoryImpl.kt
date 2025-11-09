package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.mapper.toDomain
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(private val couponDao: CouponDao): CouponRepository {
    override suspend fun getCouponFromCode(barcode: String): List<Coupon>? {
        val coupon = Coupon(
            id = "abc",
            title = "Coupon",
            discountPercentage = 10.0,
            discountCode = "DISC0001",
            createdAt = 1L,
            updatedAt = 1L,
            isActive = true,
            isSynced = true,
            description = "",
            discountValidTill = 1L,
            discountAvailedBy = "",
            discountGivenTo = "",
            discountType = "",
            invoiceNumber = ""
        )
//        return couponDao.getCouponByCode(barcode)?.map {
//            it.toDomain()
//        }
        return listOf(coupon)
    }
}