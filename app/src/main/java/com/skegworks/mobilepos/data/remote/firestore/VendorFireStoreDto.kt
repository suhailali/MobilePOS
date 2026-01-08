package com.skegworks.mobilepos.data.remote.firestore

data class VendorFireStoreDto(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val phone: String = "",
    val email: String = "",
    val zipCode: String = "",
    val gst: String = "",
    val gstPercentage: String = "",
    val currency: String = "",
    val active: Boolean = false,
    val synced: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
