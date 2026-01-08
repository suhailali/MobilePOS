package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Vendor
import com.skegworks.mobilepos.data.local.VendorEntity
import com.skegworks.mobilepos.data.remote.firestore.VendorFireStoreDto


// --- Firestore DTO -> Domain ---
fun VendorFireStoreDto.toDomain(): Vendor {
    return Vendor(
        id = id,
        name = name,
        address = address,
        city = city,
        state = state,
        country = country,
        phone = phone,
        email = email,
        zipCode = zipCode,
        gst = gst,
        gstPercentage = gstPercentage,
        currency = currency,
        isActive = active,
        isSynced = synced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// --- Domain -> Firestore DTO ---
fun Vendor.toFirestoreDto(): VendorFireStoreDto {
    return VendorFireStoreDto(
        id = id,
        name = name,
        address = address,
        city = city,
        state = state,
        country = country,
        phone = phone,
        email = email,
        zipCode = zipCode,
        gst = gst,
        gstPercentage = gstPercentage,
        currency = currency,
        active = isActive,
        synced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// --- Room Entity -> Domain ---
fun VendorEntity.toDomain(): Vendor {
    return Vendor(
        id = id,
        name = name,
        address = address,
        city = city,
        state = state,
        country = country,
        phone = phone,
        email = email,
        zipCode = zipCode,
        gst = gst,
        gstPercentage = gstPercentage,
        currency = currency,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// --- Domain -> Room Entity ---
fun Vendor.toEntity(): VendorEntity {
    return VendorEntity(
        id = id,
        name = name,
        address = address,
        city = city,
        state = state,
        country = country,
        phone = phone,
        email = email,
        zipCode = zipCode,
        gst = gst,
        gstPercentage = gstPercentage,
        currency = currency,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
