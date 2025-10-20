package com.skegworks.mobilepos.vendors

import com.skegworks.mobilepos.data.Vendor

interface VendorRepository {
    suspend fun insertVendor(vendor: Vendor)

    suspend fun getVendorById(id: String) : Vendor?

    suspend fun getAllVendors(): List<Vendor>

    suspend fun updateVendor(vendor: Vendor)

    suspend fun deleteVendor(vendor: Vendor)
}