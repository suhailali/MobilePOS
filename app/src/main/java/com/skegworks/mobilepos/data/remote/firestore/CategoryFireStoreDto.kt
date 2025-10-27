package com.skegworks.mobilepos.data.remote.firestore

data class CategoryFireStoreDto(
   val id: String = "",
   val name: String = "",
   val description: String = "",
   val isActive: Boolean = true,
   var isSynced: Boolean = false,
   val createdAt: Long = 0L,
   val updatedAt: Long = 0L,
)
