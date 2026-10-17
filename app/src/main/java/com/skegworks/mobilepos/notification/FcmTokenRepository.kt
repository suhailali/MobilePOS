package com.skegworks.mobilepos.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.skegworks.mobilepos.data.remote.firestore.FCMTokenFireStoreDto
import com.skegworks.mobilepos.sync.SyncData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FcmTokenRepository @Inject constructor(
    private val syncData: SyncData
) {

    fun registerDevice(deviceId: String, deviceName: String) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                saveToken(token, deviceId, deviceName)
            }
    }

    fun saveToken(token: String, deviceId: String, deviceName: String) {
        val fcmToken = FCMTokenFireStoreDto(
            token = token,
            deviceID = deviceId,
            deviceName = deviceName
        )
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            syncData.uploadData(
                name = "fcm_token",
                id = deviceId,
                data = fcmToken,
                onSuccess = { },
                onFailure = { }
            )
        }
    }
}