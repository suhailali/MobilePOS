package com.skegworks.mobilepos.sync

interface SyncData {
    suspend fun <T> uploadData(
        name: String,
        id: String,
        data: T,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun <T> upload(
        name: String,
        id: String,
        data: T
    ) : Result<Unit>

    suspend fun  <T> downloadAll(
        name: String,
        clazz: Class<T>
    ): Result<List<T>>
}