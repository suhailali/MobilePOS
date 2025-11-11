package com.skegworks.mobilepos.appsettings

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncAppSettingsUseCaseImpl(private val repository: AppSettingsRepository) :
    SyncAppSettingsUseCase {
    override suspend fun invoke() {
        repository.getAppSettings()?.let { appSettings ->
            repository.syncAppSettings(
                appSettings = appSettings.apply { isSynced = true },
                onSuccess = { id ->
                    Log.d("Firestore", "Added with ID: $id")
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.updateAppSettings(appSettings)
                    }
                },
                onFailure = { exception ->
                    Log.e(
                        "Firestore",
                        "Error adding document",
                        exception
                    )
                }
            )
        }
    }
}