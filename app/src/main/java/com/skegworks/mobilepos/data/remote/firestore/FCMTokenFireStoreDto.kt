package com.skegworks.mobilepos.data.remote.firestore

data class FCMTokenFireStoreDto(
    val token: String = "",
    val deviceID: String = "",
    val deviceName: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)