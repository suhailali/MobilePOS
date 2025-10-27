package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.local.CustomerEntity
import com.skegworks.mobilepos.data.remote.firestore.CustomerFireStoreDto


// Firestore DTO → Domain
fun CustomerFireStoreDto.toDomain(): Customer {
    return Customer(
        id = id,
        name = name,
        email = email,
        phone = phone,
        address = address,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// Domain → Firestore DTO
fun Customer.toFirestoreDto(): CustomerFireStoreDto {
    return CustomerFireStoreDto(
        id = id,
        name = name,
        email = email,
        phone = phone,
        address = address,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// Room Entity → Domain
fun CustomerEntity.toDomain(): Customer {
    return Customer(
        id = id,
        name = name,
        email = email,
        phone = phone,
        address = address,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// Domain → Room Entity
fun Customer.toEntity(): CustomerEntity {
    return CustomerEntity(
        id = id,
        name = name,
        email = email,
        phone = phone,
        address = address,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
