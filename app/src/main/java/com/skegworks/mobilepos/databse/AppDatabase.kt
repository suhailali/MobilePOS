package com.skegworks.mobilepos.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skegworks.mobilepos.category.CategoryDao
import com.skegworks.mobilepos.customer.CustomerDao
import com.skegworks.mobilepos.data.domain.Category
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.domain.Vendor
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.product.ProductDao
import com.skegworks.mobilepos.vendors.VendorDao

@Database(
    entities = [
        Category::class,
        Product::class,
        Vendor::class,
        Customer::class
    ], version = 8
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun vendorDao(): VendorDao
    abstract fun customerDao(): CustomerDao
}
