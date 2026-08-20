package com.skegworks.mobilepos.utils.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.first

class AppPreferenceHandlerImpl(private val context: Context): AppPreferenceHandler {

    private val Context.appPreferencesStore: DataStore<AppPreferences> by dataStore(
        fileName = "app-preferences",
        serializer = AppPreferencesSerializer
    )

    override suspend fun getLastUpdatedTimeInLong(): Long {
        return context.appPreferencesStore.data.first().lastUpdatedTimeInLong
    }

}