package com.skegworks.mobilepos.appsettings

import com.skegworks.mobilepos.data.domain.AppSettings
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.sync.SyncData
import com.skegworks.mobilepos.utils.Constants

class AppSettingsRepositoryImpl(
    private val appSettingsDao: AppSettingsDao,
    private val syncData: SyncData
) : AppSettingsRepository {
    override suspend fun getAppSettings(): AppSettings? {
        return appSettingsDao.getAppSettings()?.toDomain()
    }

    override suspend fun updateAppSettings(appSettings: AppSettings) {
        appSettingsDao.updateAppSettings(appSettings.toEntity())
    }

    override suspend fun insertAppSettings(appSettings: AppSettings) {
        appSettingsDao.insertAppSettings(appSettings.toEntity())
    }

    override suspend fun syncAppSettings(
        appSettings: AppSettings,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = Constants.FirebaseDocument.APP_SETTINGS,
            id = appSettings.id,
            data = appSettings,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}