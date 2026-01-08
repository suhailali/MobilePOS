package com.skegworks.mobilepos.data.remote.firestore

data class CategoryFireStoreDto(
   val id: String = "",
   val name: String = "",
   val description: String = "",
   val active: Boolean = true,
   var synced: Boolean = false,
   val createdAt: Long = 0L,
   val updatedAt: Long = 0L,
)
