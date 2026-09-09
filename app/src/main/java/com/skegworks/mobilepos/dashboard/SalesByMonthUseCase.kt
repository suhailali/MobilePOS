package com.skegworks.mobilepos.dashboard

interface SalesByMonthUseCase {
    suspend operator fun invoke(): List<SalesByMonth>
}