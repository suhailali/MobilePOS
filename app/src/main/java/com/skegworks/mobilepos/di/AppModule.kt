package com.skegworks.mobilepos.di

import com.skegworks.mobilepos.appsettings.AppSettingsDao
import com.skegworks.mobilepos.appsettings.AppSettingsRepository
import com.skegworks.mobilepos.appsettings.AppSettingsRepositoryImpl
import com.skegworks.mobilepos.business.BusinessDao
import com.skegworks.mobilepos.business.BusinessRepository
import com.skegworks.mobilepos.business.BusinessRepositoryImpl
import com.skegworks.mobilepos.cashcounter.CashCounterDao
import com.skegworks.mobilepos.cashcounter.CashCounterRepository
import com.skegworks.mobilepos.cashcounter.CashCounterRepositoryImpl
import com.skegworks.mobilepos.category.CategoryDao
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.category.CategoryRepositoryImpl
import com.skegworks.mobilepos.coupon.CouponDao
import com.skegworks.mobilepos.coupon.CouponRepository
import com.skegworks.mobilepos.coupon.CouponRepositoryImpl
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

    @Provides
    @Singleton
    fun providesCashCounterRepository(
        cashCounterDao: CashCounterDao,
    ): CashCounterRepository {
        return CashCounterRepositoryImpl(cashCounterDao)
    }

    @Provides
    @Singleton
    fun providesAppSettingsRepository(
        appSettingsDao: AppSettingsDao,
        syncData: SyncData
    ): AppSettingsRepository {
        return AppSettingsRepositoryImpl(appSettingsDao, syncData)
    }

    @Provides
    @Singleton
    fun providesCouponRepository(
        couponDao: CouponDao,
        syncData: SyncData
    ): CouponRepository {
        return CouponRepositoryImpl(couponDao, syncData)
    }

    @Provides
    @Singleton
    fun providesBusinessRepository(
        businessDao: BusinessDao,
    ): BusinessRepository {
        return BusinessRepositoryImpl(businessDao)
    }

//    @Provides
//    @Singleton
//    fun providesDashboardRepository(
//        productDao: ProductDao,
//        customerDao: CustomerDao,
//        invoiceDao: InvoiceDao,
//    ): DashboardRepository {
//        return DashboardRepositoryImpl(productDao, customerDao, invoiceDao)
//    }
//}


}