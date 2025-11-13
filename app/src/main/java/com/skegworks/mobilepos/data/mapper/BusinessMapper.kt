package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Business
import com.skegworks.mobilepos.data.local.BusinessEntity
import com.skegworks.mobilepos.data.remote.firestore.BusinessFireStoreDto


// Firestore DTO → Domain
fun BusinessFireStoreDto.toDomain(): Business {
    return Business(
        id = id,
        name = name,
        mobile = mobile,
        email = email,
        address = address,
        gstNumber = gstNumber,
        isSynced = isSynced
    )
}

// Domain → Firestore DTO
fun Business.toFirestoreDto(): BusinessFireStoreDto {
    return BusinessFireStoreDto(
        id = id,
        name = name,
        mobile = mobile,
        email = email,
        address = address,
        gstNumber = gstNumber,
        isSynced = isSynced
    )
}

// Room Entity → Domain
fun BusinessEntity.toDomain(): Business {
    return Business(
        id = id,
        name = name,
        mobile = mobile,
        email = email,
        address = address,
        gstNumber = gstNumber,
        isSynced = isSynced
    )
}

// Domain → Room Entity
fun Business.toEntity(): BusinessEntity {
    return BusinessEntity(
        id = id,
        name = name,
        mobile = mobile,
        email = email,
        address = address,
        gstNumber = gstNumber,
        isSynced = isSynced
    )
}
