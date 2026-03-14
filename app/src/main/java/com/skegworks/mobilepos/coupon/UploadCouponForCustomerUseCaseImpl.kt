package com.skegworks.mobilepos.coupon

import android.util.Log
import com.skegworks.mobilepos.data.domain.Coupon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadCouponForCustomerUseCaseImpl @Inject constructor(private val repository: CouponRepository) :
    UploadCouponForCustomerUseCase {
    override suspend fun invoke(coupon: Coupon) {
        repository.insertCoupon(coupon)
        repository.syncCoupon(
            coupon.apply { isSynced = true },
            onSuccess = { id ->
                Log.d("Firestore", "updated with ID: $id")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.updateCoupon(coupon)
                }
            },
            onFailure = { exception ->
                Log.e(
                    "Firestore",
                    "Error updating document",
                    exception
                )
            }
        )
    }
}