package com.skegworks.mobilepos.di

import com.skegworks.mobilepos.invoice.GenerateInvoicePdfUseCase
import com.skegworks.mobilepos.invoice.GenerateInvoicePdfUseCaseImpl
import com.skegworks.mobilepos.pos.GetProductFromBarCodeUseCaseImpl
import com.skegworks.mobilepos.pos.GetProductFromBarcodeUseCase
import com.skegworks.mobilepos.product.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun provideGetProductFromBarcodeUseCase(productRepository: ProductRepository): GetProductFromBarcodeUseCase {
        return GetProductFromBarCodeUseCaseImpl(productRepository)
    }

    @Provides
    fun provideGenerateInvoicePdfUseCase(): GenerateInvoicePdfUseCase {
        return GenerateInvoicePdfUseCaseImpl()
    }
}