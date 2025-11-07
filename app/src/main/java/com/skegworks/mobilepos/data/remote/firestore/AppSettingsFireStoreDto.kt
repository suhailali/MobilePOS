package com.skegworks.mobilepos.data.remote.firestore

import androidx.privacysandbox.ads.adservices.appsetid.AppSetId

data class AppSettingsFireStoreDto(
    val id: String = "",
    val invoiceCounter: Long = 0,
    val invoiceYear: Int = 0,
    val productSkuCounter: Long = 0,
    val isSynced: Boolean = false,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val updatedBy: String = ""
)