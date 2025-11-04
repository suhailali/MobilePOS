package com.skegworks.mobilepos.data.remote.firestore

import androidx.privacysandbox.ads.adservices.appsetid.AppSetId

data class AppSettingsFireStoreDto(
    val id: Int = 0,
    val invoiceCounter: Long = 0,
    val invoiceYear: Int = 0,
    val productSkuCounter: Long = 0
)