package com.skegworks.mobilepos.di

import com.skegworks.mobilepos.barcode.BarcodeGenerator
import com.skegworks.mobilepos.barcode.ZxingBarcodeGenerator
import com.skegworks.mobilepos.sku.SkuGenerator
import com.skegworks.mobilepos.sku.SkuGeneratorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
}