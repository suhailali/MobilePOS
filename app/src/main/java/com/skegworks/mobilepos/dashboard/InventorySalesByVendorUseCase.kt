package com.skegworks.mobilepos.dashboard

interface InventorySalesByVendorUseCase {
    suspend operator fun invoke(): List<InventorySaleByVendor>
}