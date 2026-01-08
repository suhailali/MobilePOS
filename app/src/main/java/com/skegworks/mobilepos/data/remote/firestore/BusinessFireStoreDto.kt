package com.skegworks.mobilepos.data.remote.firestore

data class BusinessFireStoreDto(
    val id: String = "",
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val address: String = "",
    val gstNumber: String = "",
    val synced: Boolean = false
)
