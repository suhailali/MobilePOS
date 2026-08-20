package com.skegworks.mobilepos.coupon

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.CouponEntity

@Dao
interface CouponDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: CouponEntity)

    @Query("SELECT * FROM coupons WHERE id = :id")
    suspend fun getCouponById(id: String): CouponEntity?

    @Query("SELECT * FROM coupons WHERE discount_code LIKE '%' || :sku || '%'")
    suspend fun getCouponByCode(sku: String): List<CouponEntity>

    @Query("SELECT * FROM coupons WHERE discount_given_to_id = :customerId ORDER BY created_at DESC")
    suspend fun getCouponByCustomer(customerId: String): List<CouponEntity>

    @Query("SELECT * FROM coupons")
    suspend fun getAllCoupons(): List<CouponEntity>

    @Update
    suspend fun updateCoupon(coupon: CouponEntity)

    @Delete
    suspend fun deleteCoupon(coupon: CouponEntity)

    @Query("SELECT * FROM coupons WHERE is_synced = 0")
    suspend fun getUnsynced(): List<CouponEntity>
    suspend fun deleteAllCoupons() {
        val coupons = getAllCoupons()
        for (coupon in coupons) {
            deleteAllCoupons()
        }
    }
}