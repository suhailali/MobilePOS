package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Category
import com.skegworks.mobilepos.data.local.CategoryEntity
import com.skegworks.mobilepos.data.remote.firestore.CategoryFireStoreDto


// Firestore DTO → Domain
fun CategoryFireStoreDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        description = description,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}

// Domain → Firestore DTO
fun Category.toFirestoreDto(): CategoryFireStoreDto {
    return CategoryFireStoreDto(
        id = id,
        name = name,
        description = description,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}

// Room Entity → Domain
fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        description = description,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}

// Domain → Room Entity
fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        description = description,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isActive = isActive,
        createdAt = createdAt
    )
}
