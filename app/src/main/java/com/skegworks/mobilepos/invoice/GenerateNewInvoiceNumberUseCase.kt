package com.skegworks.mobilepos.invoice

interface GenerateNewInvoiceNumberUseCase {
    suspend operator fun invoke(): String
}