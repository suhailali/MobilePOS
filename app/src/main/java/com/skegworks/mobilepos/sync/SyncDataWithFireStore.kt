package com.skegworks.mobilepos.sync

import com.skegworks.mobilepos.data.remote.firestore.FirestoreHelper
import javax.inject.Inject

class SyncDataWithFireStore @Inject constructor(private val firestoreHelper: FirestoreHelper) :
    SyncData {
    override suspend fun <T> uploadData(
        name: String,
        id: String,
        data: T,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreHelper.addDocument(
            collection = name,
            id = id,
            data = data,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun <T> upload(
        name: String,
        id: String,
        data: T,
    ): Result<Unit> {
        return firestoreHelper.addDocument(
            collection = name,
            id = id,
            data = data
        )
    }

    override suspend fun <T> downloadAll(
        name: String,
        clazz: Class<T>
    ): Result<List<T>> {
        return firestoreHelper.getAllDocuments(collection = name, clazz = clazz)
    }
}