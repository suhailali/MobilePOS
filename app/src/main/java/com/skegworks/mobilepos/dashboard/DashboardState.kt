package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.data.domain.Dashboard
import network.chaintech.cmpcharts.ui.barchart.config.BarData

data class DashboardState(
    var isLoading: Boolean = true,
    val dashboardValue: Dashboard? = null,
    val salesPerDay: List<BarData>? = null,
    val yStepSize:Int = 0,
    val maxRange:Int = 0,
    val totalSales: Double = 0.0,
    val averageSales: Double = 0.0
)