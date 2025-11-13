package com.skegworks.mobilepos.business

import com.skegworks.mobilepos.data.domain.Business
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
class BusinessRepositoryImpl(private val businessDao: BusinessDao): BusinessRepository {
    override suspend fun insertBusiness(business: Business) {
        businessDao.insertBusiness(business.toEntity())
    }

    override suspend fun getBusinessById(id: String): Business? {
        return businessDao.getBusinessById(id)?.toDomain()
    }
}