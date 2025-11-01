package com.skegworks.mobilepos.utils.preferences

import com.skegworks.mobilepos.data.domain.UserRole

interface UserPreferenceHandler {
    suspend fun getUserRole(): UserRole?

    suspend fun saveUser(email: String, role: UserRole)
}