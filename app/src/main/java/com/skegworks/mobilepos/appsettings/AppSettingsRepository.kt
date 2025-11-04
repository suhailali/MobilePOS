package com.skegworks.mobilepos.appsettings

import com.skegworks.mobilepos.data.domain.AppSettings

interface AppSettingsRepository {
    suspend fun getAppSettings(): AppSettings?
    suspend fun updateAppSettings(appSettings: AppSettings)
}