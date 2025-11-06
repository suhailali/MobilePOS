package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.local.CouponEntity
import com.skegworks.mobilepos.data.remote.firestore.CouponFireStoreDto


// Firestore DTO → Domain
fun CouponFireStoreDto.toDomain(): Coupon {
    return Coupon(
        id = id,
        title = title,
        description = description,
        discountCode = discountCode,
        discountType = discountType,
        discountPercentage = discountPercentage,
        discountGivenTo = discountGivenTo,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}

// Domain → Firestore DTO
fun Coupon.toFirestoreDto(): CouponFireStoreDto {
    return CouponFireStoreDto(
        id = id,
        title = title,
        description = description,
        discountCode = discountCode,
        discountType = discountType,
        discountPercentage = discountPercentage,
        discountGivenTo = discountGivenTo,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}

// Room Entity → Domain
fun CouponEntity.toDomain(): Coupon {
    return Coupon(
        id = id,
        title = title,
        description = description,
        discountCode = discountCode,
        discountType = discountType,
        discountPercentage = discountPercentage,
        discountGivenTo = discountGivenTo,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}

// Domain → Room Entity
fun Coupon.toEntity(): CouponEntity {
    return CouponEntity(
        id = id,
        title = title,
        description = description,
        discountCode = discountCode,
        discountType = discountType,
        discountPercentage = discountPercentage,
        discountGivenTo = discountGivenTo,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}
