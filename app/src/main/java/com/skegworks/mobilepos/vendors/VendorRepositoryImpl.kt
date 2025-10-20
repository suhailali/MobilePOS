package com.skegworks.mobilepos.vendors

import com.skegworks.mobilepos.data.Vendor
import javax.inject.Inject

class VendorRepositoryImpl @Inject constructor(private val vendorDao: VendorDao) : VendorRepository {
    override suspend fun insertVendor(vendor: Vendor) {
        vendorDao.insertVendor(vendor)
    }

    override suspend fun getVendorById(id: String): Vendor? {
        return vendorDao.getVendorById(id)
    }

    override suspend fun getAllVendors(): List<Vendor> {
        return vendorDao.getAllVendors()
    }

    override suspend fun updateVendor(vendor: Vendor) {
        vendorDao.updateVendor(vendor)
    }

    override suspend fun deleteVendor(vendor: Vendor) {
        vendorDao.deleteVendor(vendor)
    }
}