package com.skegworks.mobilepos.appsettings

interface GenerateNewProductCounterUseCase {
    suspend operator fun invoke(): Long
}