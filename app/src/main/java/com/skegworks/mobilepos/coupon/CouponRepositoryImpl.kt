package com.skegworks.mobilepos.coupon

import com.skegworks.mobilepos.coupon.CouponDao
import com.skegworks.mobilepos.coupon.CouponRepository
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(private val couponDao: CouponDao, private val syncData: SyncData): CouponRepository {
    override suspend fun insertCoupon(coupon: Coupon) {
        couponDao.insertCoupon(coupon.toEntity())
    }

    override suspend fun getCouponById(id: String): Coupon? {
        return couponDao.getCouponById(id)?.toDomain()
    }

    override suspend fun getAllCoupons(): List<Coupon> {
        return couponDao.getAllCoupons().map {
            it.toDomain()
        }
    }

    override suspend fun updateCoupon(coupon: Coupon) {
        couponDao.updateCoupon(coupon.toEntity())
    }

    override suspend fun deleteCoupon(coupon: Coupon) {
        couponDao.deleteCoupon(coupon.toEntity())
    }

    override suspend fun deleteAllCoupons() {
        couponDao.deleteAllCoupons()
    }

    override suspend fun syncCoupon(coupon: Coupon, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        syncData.uploadData(
            name = "coupons",
            id = coupon.id,
            data = coupon.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllCoupons(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        //TODO Transform model if required
        val coupons = couponDao.getAllCoupons()
        for (coupon in coupons) {
            syncData.uploadData(
                name = "coupons",
                id = coupon.id,
                data = coupon,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
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