package com.skegworks.mobilepos.appsettings

import com.skegworks.mobilepos.data.domain.AppSettings
import com.skegworks.mobilepos.utils.DateUtility
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import javax.inject.Inject

class InitialiseAppSettingsUseCaseImpl @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val utility: DateUtility,
    private val uuidGenerator: UUIDGenerator,
    private val userPreferenceHandler: UserPreferenceHandler
) : InitialiseAppSettingsUseCase {

    override suspend fun invoke() {
        var appSettings = appSettingsRepository.getAppSettings()
        val currentYear = utility.getYear()
        var newCounter = 0L
        if (appSettings != null) {
            val year = appSettings.invoiceYear
            newCounter = if (year != currentYear) {
                0L
            } else {
                appSettings.invoiceCounter
            }
            appSettingsRepository.updateAppSettings(
                appSettings.copy(
                    invoiceCounter = newCounter,
                    invoiceYear = currentYear
                )
            )
        } else {
            appSettings = AppSettings(
                id = uuidGenerator.generateUUID(),
                invoiceCounter = 0,
                invoiceYear = currentYear,
                productSkuCounter = 0,
                isSynced = false,
                createdAt = utility.getCurrentTimeStamp(),
                updatedAt = utility.getCurrentTimeStamp(),
                updatedBy = userPreferenceHandler.getUserEmail() ?: ""
            )
            appSettingsRepository.insertAppSettings(appSettings)
        }
    }
}