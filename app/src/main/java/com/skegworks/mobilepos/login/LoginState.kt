package com.skegworks.mobilepos.login

data class LoginState(
    val textStateEmail: String = "",
    val textStatePassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)