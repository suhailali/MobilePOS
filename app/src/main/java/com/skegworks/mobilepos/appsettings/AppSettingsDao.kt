package com.skegworks.mobilepos.appsettings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.AppSettingsEntity

@Dao
interface AppSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppSettings(appSettings: AppSettingsEntity)

    @Update
    suspend fun updateAppSettings(appSettings: AppSettingsEntity)

    @Query("SELECT * FROM app_settings")
    suspend fun getAppSettings(): AppSettingsEntity?

    @Query("DELETE FROM app_settings")
    suspend fun deleteAppSettings()
}
