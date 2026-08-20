package com.skegworks.mobilepos.login

import com.skegworks.mobilepos.data.domain.UserRole

data class LoginState(
    val textStateEmail: String = "suhailali.offl@gmail.com",
    val textStatePassword: String = "1234567890",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loadingMessage: String? = null,
    val success: Boolean = false,
    val userRole: UserRole? = null
)