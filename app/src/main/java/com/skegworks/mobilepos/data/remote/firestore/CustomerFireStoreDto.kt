package com.skegworks.mobilepos.data.remote.firestore

data class CustomerFireStoreDto(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val isActive: Boolean = true,
    var isSynced: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
