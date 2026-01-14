package com.skegworks.mobilepos.coupon

import android.util.Log
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.utils.DateUtility
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateCouponPostInvoiceImpl @Inject constructor(
    private val couponRepository: CouponRepository,
    private val userPreferenceHandler: UserPreferenceHandler,
    private val dateUtility: DateUtility): UpdateCouponPostInvoice {
    override suspend fun invoke(
        coupon: Coupon,
        invoice: Invoice
    ) {
        val updatedCoupon = coupon.copy(
            discountAvailedBy = invoice.customer.name,
            discountAvailedOn = dateUtility.getDateTime(),
            discountedAmount = invoice.couponDiscount,
            invoiceNumber = invoice.invoiceNumber,
            invoiceId = invoice.id,
            isActive = false,
            isSynced = false,
            updatedAt = System.currentTimeMillis(),
            updatedBy = userPreferenceHandler.getUserEmail() ?: ""
        )
        couponRepository.updateCoupon(updatedCoupon)
        couponRepository.syncCoupon(updatedCoupon.apply { isSynced = true },
        onSuccess = { id ->
            Log.d("Firestore", "updated with ID: $id")
            CoroutineScope(Dispatchers.IO).launch {
                couponRepository.updateCoupon(updatedCoupon)
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