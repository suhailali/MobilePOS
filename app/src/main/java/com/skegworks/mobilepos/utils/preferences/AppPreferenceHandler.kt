package com.skegworks.mobilepos.utils.preferences

interface AppPreferenceHandler {
    suspend fun getLastUpdatedTimeInLong() : Long
}