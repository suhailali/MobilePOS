package com.skegworks.mobilepos.data.remote.firestore

data class CashCounterFireStoreDto(
    val id: String = "",
    val date: String = "",
    val balance: Double = 0.0,
    val status: String = "",
    val createdAt: Long = 0L,
    val createdBy: String = "",
    val isSynced: Boolean = false,
)