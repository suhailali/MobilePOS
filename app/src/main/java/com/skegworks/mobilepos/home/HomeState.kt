package com.skegworks.mobilepos.home

import com.skegworks.mobilepos.data.domain.UserRole

data class HomeState(
    val businessName: String ="",
    var userRole: UserRole? = null,
    var userEmail: String? = null,
    var features: List<String> = listOf()
)