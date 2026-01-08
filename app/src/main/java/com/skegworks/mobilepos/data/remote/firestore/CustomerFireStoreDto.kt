package com.skegworks.mobilepos.data.remote.firestore

data class CustomerFireStoreDto(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val active: Boolean = true,
    var synced: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
