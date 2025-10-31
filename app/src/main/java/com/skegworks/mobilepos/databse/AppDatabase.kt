package com.skegworks.mobilepos.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skegworks.mobilepos.category.CategoryDao
import com.skegworks.mobilepos.customer.CustomerDao
import com.skegworks.mobilepos.data.domain.Category
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.domain.Vendor
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.local.BusinessEntity
import com.skegworks.mobilepos.data.local.CategoryEntity
import com.skegworks.mobilepos.data.local.CustomerEntity
import com.skegworks.mobilepos.data.local.InvoiceEntity
import com.skegworks.mobilepos.data.local.InvoiceItemEntity
import com.skegworks.mobilepos.data.local.ProductEntity
import com.skegworks.mobilepos.data.local.VendorEntity
import com.skegworks.mobilepos.invoice.InvoiceDao
import com.skegworks.mobilepos.invoice.InvoiceItemDao
import com.skegworks.mobilepos.product.ProductDao
import com.skegworks.mobilepos.vendors.VendorDao

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        VendorEntity::class,
        CustomerEntity::class,
        BusinessEntity::class,
        InvoiceEntity::class,
        InvoiceItemEntity::class
    ], version = 13
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun vendorDao(): VendorDao
    abstract fun customerDao(): CustomerDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun invoiceItemDao(): InvoiceItemDao


}
