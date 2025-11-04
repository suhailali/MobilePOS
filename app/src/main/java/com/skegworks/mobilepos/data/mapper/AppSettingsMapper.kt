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
        productSkuCounter = productSkuCounter
    )
}

// Domain → Firestore DTO
fun AppSettings.toFirestoreDto(): AppSettingsFireStoreDto {
    return AppSettingsFireStoreDto(
        id = id,
        invoiceCounter = invoiceCounter,
        invoiceYear = invoiceYear,
        productSkuCounter = productSkuCounter
    )
}

// Room Entity → Domain
fun AppSettingsEntity.toDomain(): AppSettings {
    return AppSettings(
        id = id,
        invoiceCounter = invoiceCounter,
        invoiceYear = invoiceYear,
        productSkuCounter = productSkuCounter
    )
}

// Domain → Room Entity
fun AppSettings.toEntity(): AppSettingsEntity {
    return AppSettingsEntity(
        id = id,
        invoiceCounter = invoiceCounter,
        invoiceYear = invoiceYear,
        productSkuCounter = productSkuCounter
    )
}
