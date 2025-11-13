package com.skegworks.mobilepos.business

import com.skegworks.mobilepos.data.domain.Business

interface BusinessRepository {
    suspend fun insertBusiness(business: Business)

    suspend fun getBusiness() : Business?
}