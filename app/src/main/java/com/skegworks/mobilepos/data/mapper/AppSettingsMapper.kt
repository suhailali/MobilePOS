package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.AppSettings
import com.skegworks.mobilepos.data.local.AppSettingsEntity
import com.skegworks.mobilepos.data.remote.firestore.AppSettingsFireStoreDto


// Firestore DTO → Domain
fun AppSettingsFireStoreDto.toDomain(): AppSettings {
    return AppSettings(
        id = id,
        invoiceCounter = invoiceCounter,
        invoiceYear = invoiceYear,
        productSkuCounter = productSkuCounter,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy
    )
}

// Domain → Firestore DTO
fun AppSettings.toFirestoreDto(): AppSettingsFireStoreDto {
    return AppSettingsFireStoreDto(
        id = id,
        invoiceCounter = invoiceCounter,
        invoiceYear = invoiceYear,
        productSkuCounter = productSkuCounter,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy
    )
}

// Room Entity → Domain
fun AppSettingsEntity.toDomain(): AppSettings {
    return AppSettings(
        id = id,
        invoiceCounter = invoiceCounter,
        invoiceYear = invoiceYear,
        productSkuCounter = productSkuCounter,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy
    )
}

// Domain → Room Entity
fun AppSettings.toEntity(): AppSettingsEntity {
    return AppSettingsEntity(
        id = id,
        invoiceCounter = invoiceCounter,
        invoiceYear = invoiceYear,
        productSkuCounter = productSkuCounter,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        updatedBy = updatedBy
    )
}
