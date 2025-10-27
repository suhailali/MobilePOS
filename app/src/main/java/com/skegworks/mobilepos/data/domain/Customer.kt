package com.skegworks.mobilepos.data.domain

data class Customer(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val isActive: Boolean = true,
    var isSynced: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)
