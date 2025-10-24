package com.skegworks.mobilepos.sync

interface SyncData {
    suspend fun <T> syncData(
        name: String,
        id: String,
        data: T,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}