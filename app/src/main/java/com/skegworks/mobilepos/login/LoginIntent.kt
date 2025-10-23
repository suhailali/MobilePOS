package com.skegworks.mobilepos.login

sealed class LoginIntent {
    data class UpdateUsername(val username: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    data class SubmitLogin(val isCreateUser: Boolean) : LoginIntent()
}