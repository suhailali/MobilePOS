package com.skegworks.mobilepos.appsettings

import javax.inject.Inject

class GenerateNewProductCounterUseCaseImpl @Inject constructor(private val appSettingsRepository: AppSettingsRepository): GenerateNewProductCounterUseCase {
    override suspend fun invoke(): Long {
        val appSettings = appSettingsRepository.getAppSettings()
        if (appSettings !=null ) {
            val newCounter = appSettings.productSkuCounter + 1
            return newCounter
        } else {
            return 1
        }
    }
}