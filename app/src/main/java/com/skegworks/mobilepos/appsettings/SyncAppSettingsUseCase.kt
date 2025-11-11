package com.skegworks.mobilepos.appsettings

interface SyncAppSettingsUseCase {
    suspend operator fun invoke()
}