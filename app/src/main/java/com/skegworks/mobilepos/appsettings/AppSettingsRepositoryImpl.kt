package com.skegworks.mobilepos.appsettings

import com.skegworks.mobilepos.data.domain.AppSettings
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity

class AppSettingsRepositoryImpl(private val appSettingsDao: AppSettingsDao): AppSettingsRepository {
    override suspend fun getAppSettings(): AppSettings? {
        return appSettingsDao.getAppSettings()?.toDomain()
    }

    override suspend fun updateAppSettings(appSettings: AppSettings) {
        appSettingsDao.updateAppSettings(appSettings.toEntity())
    }
}