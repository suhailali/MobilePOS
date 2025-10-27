package com.skegworks.mobilepos.vendors

import com.skegworks.mobilepos.data.domain.Vendor
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData
import javax.inject.Inject

class VendorRepositoryImpl @Inject constructor(private val vendorDao: VendorDao, private val syncData: SyncData) : VendorRepository {
    override suspend fun insertVendor(vendor: Vendor) {
        vendorDao.insertVendor(vendor.toEntity())
    }

    override suspend fun getVendorById(id: String): Vendor? {
        return vendorDao.getVendorById(id)?.toDomain()
    }

    override suspend fun getAllVendors(): List<Vendor> {
        return vendorDao.getAllVendors().map {
            it.toDomain()
        }
    }

    override suspend fun updateVendor(vendor: Vendor) {
        vendorDao.updateVendor(vendor.toEntity())
    }

    override suspend fun deleteVendor(vendor: Vendor) {
        vendorDao.deleteVendor(vendor.toEntity())
    }

    override suspend fun syncVendor(vendor: Vendor, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        syncData.uploadData(
            name = "vendors",
            id = vendor.id,
            data = vendor.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllCategories(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        //TODO do transformation if required
        val vendors = vendorDao.getAllVendors()
        for (vendor in vendors) {
            syncData.uploadData(
                name = "categories",
                id = vendor.id,
                data = vendor,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
}