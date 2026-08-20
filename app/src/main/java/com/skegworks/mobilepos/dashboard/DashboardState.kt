package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.data.domain.ChartPoint
import com.skegworks.mobilepos.data.domain.Dashboard

data class DashboardState(
    var isLoading: Boolean = false,
    val dashboardValue: Dashboard? = null,
    val salesPerDay: List<ChartPoint>? = null
)