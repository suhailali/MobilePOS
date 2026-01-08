package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.local.CouponEntity
import com.skegworks.mobilepos.data.remote.firestore.CouponFireStoreDto
import kotlin.Long


// Firestore DTO → Domain
fun CouponFireStoreDto.toDomain(): Coupon {
    return Coupon(
        id = id,
        title = title,
        description = description,
        discountCode = discountCode,
        discountType = discountType,
        discountPercentage = discountPercentage,
        discountGivenToName = discountGivenToName,
        discountGivenToId = discountGivenToId,
        discountValidTill = discountValidTill,
        discountAvailedBy = discountAvailedBy,
        discountAvailedOn = discountAvailedOn,
        discountedAmount = discountedAmount,
        invoiceNumber = invoiceNumber,
        invoiceId = invoiceId,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        isSynced =  synced,
        isDeleted = deleted,
        isActive = active,
        createdAt = createdAt,
        createdBy = createdBy
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
        discountGivenToName = discountGivenToName,
        discountGivenToId = discountGivenToId,
        discountValidTill = discountValidTill,
        discountAvailedBy = discountAvailedBy,
        discountAvailedOn = discountAvailedOn,
        discountedAmount = discountedAmount,
        invoiceNumber = invoiceNumber,
        invoiceId = invoiceId,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        synced = isSynced,
        deleted = isDeleted,
        active = isActive,
        createdAt = createdAt,
        createdBy = createdBy
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
        discountGivenToName = discountGivenToName,
        discountGivenToId = discountGivenToId,
        discountValidTill = discountValidTill,
        discountAvailedBy = discountAvailedBy,
        discountAvailedOn = discountAvailedOn,
        discountedAmount = discountedAmount,
        invoiceNumber = invoiceNumber,
        invoiceId = invoiceId,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        isSynced = isSynced,
        isDeleted = isDeleted,
        isActive = isActive,
        createdAt = createdAt,
        createdBy = createdBy
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
        discountGivenToName = discountGivenToName,
        discountGivenToId = discountGivenToId,
        discountValidTill = discountValidTill,
        discountAvailedBy = discountAvailedBy,
        discountAvailedOn = discountAvailedOn,
        discountedAmount = discountedAmount,
        invoiceNumber = invoiceNumber,
        invoiceId = invoiceId,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        isSynced = isSynced,
        isDeleted = isDeleted,
        isActive = isActive,
        createdAt = createdAt,
        createdBy = createdBy
    )
}
