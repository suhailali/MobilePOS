package com.skegworks.mobilepos.appsettings

import javax.inject.Inject

class UpdateProductCounterUseCaseImpl @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) : UpdateProductCounterUseCase {

    override suspend fun invoke() {
        val appSettings = appSettingsRepository.getAppSettings()
        var newCounter = 1L
        appSettings?.let {
            newCounter = appSettings.productSkuCounter
            newCounter++
            appSettingsRepository.updateAppSettings(
                it.copy(
                    productSkuCounter = newCounter
                )
            )
        }
    }
}