package com.skegworks.mobilepos.di

import com.skegworks.mobilepos.category.CategoryDao
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.category.CategoryRepositoryImpl
import com.skegworks.mobilepos.customer.CustomerDao
import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.customer.CustomerRepositoryImpl
import com.skegworks.mobilepos.product.ProductDao
import com.skegworks.mobilepos.product.ProductRepository
import com.skegworks.mobilepos.product.ProductRepositoryImpl
import com.skegworks.mobilepos.vendors.VendorDao
import com.skegworks.mobilepos.vendors.VendorRepository
import com.skegworks.mobilepos.vendors.VendorRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        productDao: ProductDao
    ): ProductRepository {
        return ProductRepositoryImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryDao: CategoryDao
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

    @Provides
    @Singleton
    fun provideVendorRepository(
        vendorDao: VendorDao
    ): VendorRepository {
        return VendorRepositoryImpl(vendorDao)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(
        customerDao: CustomerDao
    ): CustomerRepository {
        return CustomerRepositoryImpl(customerDao)
    }
}