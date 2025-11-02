package com.skegworks.mobilepos.utils.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.skegworks.mobilepos.data.domain.UserRole
import kotlinx.coroutines.flow.first

class UserPreferenceHandlerImpl(private val context: Context): UserPreferenceHandler {

    private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
        fileName = "user-preferences",
        serializer = UserPreferencesSerializer
    )

    // A one-shot function to get the current role, useful for non-UI logic
    suspend fun Context.fetchUserRole(): UserRole? {
        return userPreferencesStore.data.first().role
    }

    suspend fun Context.fetchUserEmail(): String? {
        return userPreferencesStore.data.first().email
    }

    override suspend fun getUserRole(): UserRole? {
        return context.fetchUserRole()
    }

    override suspend fun getUserEmail(): String? {
        return context.fetchUserEmail()
    }

    override suspend fun saveUser(
        email: String,
        role: UserRole
    ) {
        context.userPreferencesStore.updateData {
            UserPreferences(email, role)
        }
    }
}