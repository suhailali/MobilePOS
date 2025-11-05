package com.skegworks.mobilepos.appsettings

interface GenerateNewInvoiceNumberUseCase {
    suspend operator fun invoke(): String
}