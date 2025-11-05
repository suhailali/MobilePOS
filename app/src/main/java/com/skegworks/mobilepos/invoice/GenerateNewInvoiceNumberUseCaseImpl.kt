package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.appsettings.AppSettingsRepository
import com.skegworks.mobilepos.utils.DateUtility
import javax.inject.Inject

class GenerateNewInvoiceNumberUseCaseImpl @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val utility: DateUtility) :
    GenerateNewInvoiceNumberUseCase {

    override suspend fun invoke(): String {
        val appSettings = appSettingsRepository.getAppSettings()
        val currentYear = utility.getYear()
        var newCounter = 1L
        if (appSettings != null) {
           newCounter = appSettings.invoiceCounter
            val year = appSettings.invoiceYear
            if (year != currentYear) {
                newCounter = 1
            } else {
                newCounter++
            }
        }
        return buildString {
            append(newCounter.toString().padStart(4, '0'))
            append("/")
            append(currentYear)
        }
    }
}