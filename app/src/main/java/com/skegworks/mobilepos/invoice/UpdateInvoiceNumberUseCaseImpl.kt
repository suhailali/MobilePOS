package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.appsettings.AppSettingsRepository
import com.skegworks.mobilepos.utils.DateUtility
import javax.inject.Inject

class UpdateInvoiceNumberUseCaseImpl @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val utility: DateUtility
) :
    UpdateInvoiceNumberUseCase {

    override suspend fun invoke() {
        val appSettings = appSettingsRepository.getAppSettings()
        val currentYear = utility.getYear()
        var newCounter = 1L
        appSettings?.let {
            newCounter = appSettings.invoiceCounter
            val year = appSettings.invoiceYear
            if (year != currentYear) {
                newCounter = 1
            } else {
                newCounter++
            }
            appSettingsRepository.updateAppSettings(
                it.copy(
                    invoiceCounter = newCounter,
                    invoiceYear = currentYear
                )
            )
        }
    }
}