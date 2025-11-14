package com.skegworks.mobilepos.data.remote.firestore

data class CashCounterFireStoreDto(
    val id: String = "",
    val date: String = "",
    val status: String = "",
    val isSynced: Boolean = false,
    val openingBalance: Double = 0.0,
    val closingBalance: Double = 0.0,
    val openedAt: Long = 0L,
    val openedBy: String = "",
    val closedAt: Long = 0L,
    val closedBy: String = "",
)