package com.skegworks.mobilepos.home

import com.skegworks.mobilepos.data.domain.UserRole

data class HomeState(
    var userRole: UserRole? = null,
    var features: List<String> = listOf()
)