package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.CashCounter
import com.skegworks.mobilepos.data.domain.CashCounterStatus
import com.skegworks.mobilepos.data.local.CashCounterEntity
import com.skegworks.mobilepos.data.remote.firestore.CashCounterFireStoreDto


// Firestore DTO → Domain
fun CashCounterFireStoreDto.toDomain(): CashCounter {
    return CashCounter(
        id = id,
        date = date,
        balance = balance,
        status = CashCounterStatus.fromStatus(status) ?: CashCounterStatus.CLOSED,
        createdAt = createdAt,
        createdBy = createdBy,
        isSynced = isSynced
    )
}

// Domain → Firestore DTO
fun CashCounter.toFirestoreDto(): CashCounterFireStoreDto {
    return CashCounterFireStoreDto(
        id = id,
        date = date,
        balance = balance,
        status = status.name,
        createdAt = createdAt,
        createdBy = createdBy,
        isSynced = isSynced
    )
}

// Room Entity → Domain
fun CashCounterEntity.toDomain(): CashCounter {
    return CashCounter(
        id = id,
        date = date,
        balance = balance,
        status = CashCounterStatus.fromStatus(status) ?: CashCounterStatus.CLOSED,
        createdAt = createdAt,
        createdBy = createdBy,
        isSynced = isSynced
    )
}

// Domain → Room Entity
fun CashCounter.toEntity(): CashCounterEntity {
    return CashCounterEntity(
        id = id,
        date = date,
        balance = balance,
        status = status.name,
        createdAt = createdAt,
        createdBy = createdBy,
        isSynced = isSynced
    )
}
