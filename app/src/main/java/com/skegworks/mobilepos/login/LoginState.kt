package com.skegworks.mobilepos.login

import com.skegworks.mobilepos.data.domain.UserRole

data class LoginState(
    val textStateEmail: String = "nadhika.staff1@gmail.com",
    val textStatePassword: String = "Staff@Nadhika001",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val userRole: UserRole? = null
)