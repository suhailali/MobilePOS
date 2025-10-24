package com.skegworks.mobilepos.sync

import com.skegworks.mobilepos.data.firestore.FirestoreHelper
import javax.inject.Inject

class SyncDataWithFireStore @Inject constructor(private val firestoreHelper: FirestoreHelper): SyncData {
    override suspend fun <T> syncData(
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
}