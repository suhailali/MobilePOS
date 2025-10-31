package com.skegworks.mobilepos.di

import com.skegworks.mobilepos.category.CategoryDao
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.category.CategoryRepositoryImpl
import com.skegworks.mobilepos.customer.CustomerDao
import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.customer.CustomerRepositoryImpl
import com.skegworks.mobilepos.invoice.InvoiceDao
import com.skegworks.mobilepos.invoice.InvoiceItemDao
import com.skegworks.mobilepos.invoice.InvoiceRepository
import com.skegworks.mobilepos.invoice.InvoiceRepositoryImpl
import com.skegworks.mobilepos.product.ProductDao
import com.skegworks.mobilepos.product.ProductRepository
import com.skegworks.mobilepos.product.ProductRepositoryImpl
import com.skegworks.mobilepos.sync.SyncData
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
        productDao: ProductDao,
        syncData: SyncData
    ): ProductRepository {
        return ProductRepositoryImpl(productDao, syncData)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryDao: CategoryDao,
        syncData: SyncData
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao, syncData)
    }

    @Provides
    @Singleton
    fun provideVendorRepository(
        vendorDao: VendorDao,
        syncData: SyncData
    ): VendorRepository {
        return VendorRepositoryImpl(vendorDao, syncData)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(
        customerDao: CustomerDao,
        syncData: SyncData
    ): CustomerRepository {
        return CustomerRepositoryImpl(customerDao, syncData)
    }

    @Provides
    @Singleton
    fun providesInvoiceRepository(
        invoiceDao: InvoiceDao,
        invoiceItemDao: InvoiceItemDao,
        syncData: SyncData
    ): InvoiceRepository {
        return InvoiceRepositoryImpl(invoiceDao, invoiceItemDao, syncData)
    }

}