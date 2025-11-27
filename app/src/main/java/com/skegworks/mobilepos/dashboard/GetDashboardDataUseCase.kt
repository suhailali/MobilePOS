package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.data.domain.Dashboard

interface GetDashboardDataUseCase {
    suspend operator fun invoke(): Dashboard
}