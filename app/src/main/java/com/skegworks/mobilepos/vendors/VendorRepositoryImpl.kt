package com.skegworks.mobilepos.vendors

import com.skegworks.mobilepos.data.Vendor
import com.skegworks.mobilepos.sync.SyncData
import javax.inject.Inject

class VendorRepositoryImpl @Inject constructor(private val vendorDao: VendorDao, private val syncData: SyncData) : VendorRepository {
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

    override suspend fun syncVendor(vendor: Vendor, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        syncData.syncData(
            name = "vendors",
            id = vendor.id,
            data = vendor,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllCategories(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val vendors = vendorDao.getAllVendors()
        for (vendor in vendors) {
            syncData.syncData(
                name = "categories",
                id = vendor.id,
                data = vendor,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
}