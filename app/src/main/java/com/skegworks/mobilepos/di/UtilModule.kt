package com.skegworks.mobilepos.di

import android.content.Context
import com.skegworks.mobilepos.barcode.BarcodeGenerator
import com.skegworks.mobilepos.barcode.ZxingBarcodeGenerator
import com.skegworks.mobilepos.data.remote.firestore.FirestoreHelper
import com.skegworks.mobilepos.sku.SkuGenerator
import com.skegworks.mobilepos.sku.SkuGeneratorImpl
import com.skegworks.mobilepos.sync.SyncData
import com.skegworks.mobilepos.sync.SyncDataWithFireStore
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.UUIDGeneratorImpl
import com.skegworks.mobilepos.utils.files.FileHandler
import com.skegworks.mobilepos.utils.files.FileHandlerImpl
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilModule {
    @Provides
    fun provideSkuGenerator(): SkuGenerator {
        return SkuGeneratorImpl()
    }

    @Provides
    fun providesBarcodeGenerator(): BarcodeGenerator {
        return ZxingBarcodeGenerator()
    }

    @Singleton
    @Provides
    fun provideSyncData(firestoreHelper: FirestoreHelper): SyncData {
        return SyncDataWithFireStore(firestoreHelper)
    }

    @Singleton
    @Provides
    fun provideFirestoreHelper(): FirestoreHelper {
        return FirestoreHelper()
    }

    @Singleton
    @Provides
    fun provideUUIDGenerator(): UUIDGenerator {
        return UUIDGeneratorImpl()
    }

    @Provides
    fun providesFileHandler(@ApplicationContext context: Context): FileHandler {
        return FileHandlerImpl(context)
    }

    @Singleton
    @Provides
    fun providesUserPreferenceHandler(@ApplicationContext context: Context): UserPreferenceHandler {
        return UserPreferenceHandlerImpl(context)
    }
}