package com.skegworks.mobilepos.di

import android.content.Context
import androidx.room.Room
import com.skegworks.mobilepos.appsettings.AppSettingsDao
import com.skegworks.mobilepos.cashcounter.CashCounterDao
import com.skegworks.mobilepos.category.CategoryDao
import com.skegworks.mobilepos.customer.CustomerDao
import com.skegworks.mobilepos.databse.AppDatabase
import com.skegworks.mobilepos.invoice.InvoiceDao
import com.skegworks.mobilepos.invoice.InvoiceItemDao
import com.skegworks.mobilepos.product.ProductDao
import com.skegworks.mobilepos.vendors.VendorDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mobilepos-db"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoryDao()
    }

    @Provides
    fun provideVendorDao(db: AppDatabase): VendorDao {
        return db.vendorDao()
    }

    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao {
        return db.customerDao()
    }

    @Provides
    fun provideInvoiceDao(db: AppDatabase): InvoiceDao {
        return db.invoiceDao()
    }

    @Provides
    fun provideInvoiceItemDao(db: AppDatabase): InvoiceItemDao {
        return db.invoiceItemDao()
    }

    @Provides
    fun provideCashCounterDao(db: AppDatabase): CashCounterDao {
        return db.cashCounterDao()
    }

    @Provides
    fun provideAppSettingsDao(db: AppDatabase): AppSettingsDao {
        return db.appSettingsDao()
    }
}