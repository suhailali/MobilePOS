package com.skegworks.mobilepos.login

import com.skegworks.mobilepos.data.domain.UserRole

data class LoginState(
    val textStateEmail: String = "",
    val textStatePassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val userRole: UserRole? = null
)