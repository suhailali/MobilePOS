package com.skegworks.mobilepos.data.domain

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    var isSynced: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)
