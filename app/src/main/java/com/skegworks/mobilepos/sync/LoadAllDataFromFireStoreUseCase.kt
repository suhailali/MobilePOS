package com.skegworks.mobilepos.sync

import com.skegworks.mobilepos.appsettings.AppSettingsRepository
import com.skegworks.mobilepos.business.BusinessRepository
import com.skegworks.mobilepos.cashcounter.CashCounterRepository
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.coupon.CouponRepository
import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.remote.firestore.AppSettingsFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.BusinessFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.CashCounterFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.CategoryFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.CouponFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.CustomerFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.ProductFireStoreDto
import com.skegworks.mobilepos.data.remote.firestore.VendorFireStoreDto
import com.skegworks.mobilepos.product.ProductRepository
import com.skegworks.mobilepos.utils.Constants
import com.skegworks.mobilepos.vendors.VendorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class LoadAllDataFromFireStoreUseCase @Inject constructor(
    private val syncDataWithFireStore: SyncData,
    private val categoryRepository: CategoryRepository,
    private val customerRepository: CustomerRepository,
    private val vendorRepository: VendorRepository,
    private val productRepository: ProductRepository,
    private val couponRepository: CouponRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val cashCounterRepository: CashCounterRepository,
    private val businessRepository: BusinessRepository
) {
    private val supervisor = SupervisorJob()
    private val scope = CoroutineScope(supervisor + Dispatchers.IO)

    operator fun invoke(onCompletion: () -> Unit) {
        scope.launch {
            supervisorScope {
                val tasks = listOf(
                    async {
                        try {
                            getAppSettings()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getBusiness()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getCategories()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getProducts()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getCustomers()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getVendors()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getCoupons()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                    async {
                        try {
                            getCashCounter()
                        } catch (ex: Exception) {
                            // Log or handle the exception as needed
                        }
                    },
                )
                tasks.awaitAll()
                withContext(Dispatchers.Main) {
                    onCompletion()
                }
            }
        }
    }

    private suspend fun getAppSettings() {
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.APP_SETTINGS,
            AppSettingsFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (appSetting in result.getOrNull().orEmpty()) {
                appSettingsRepository.insertAppSettings(appSetting.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading appSettings")
        }
    }

    private suspend fun getBusiness() {
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.BUSINESS,
            BusinessFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (business in result.getOrNull().orEmpty()) {
                businessRepository.insertBusiness(business.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading Business")
        }
    }

    private suspend fun getCategories() {
        val result =
            syncDataWithFireStore.downloadAll(
                Constants.FirebaseDocument.CATEGORIES,
                CategoryFireStoreDto::class.java
            )
        if (result.isSuccess) {
            for (category in result.getOrNull().orEmpty()) {
                categoryRepository.insertCategory(category.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading categories")
        }
    }

    private suspend fun getProducts() {
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.PRODUCTS,
            ProductFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (product in result.getOrNull().orEmpty()) {
                productRepository.insertProduct(product.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading product")
        }
    }

    private suspend fun getCustomers() {
        val result =
            syncDataWithFireStore.downloadAll(
                Constants.FirebaseDocument.CUSTOMERS,
                CustomerFireStoreDto::class.java
            )
        if (result.isSuccess) {
            for (customer in result.getOrNull().orEmpty()) {
                customerRepository.insertCustomer(customer.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading customers")
        }
    }

    private suspend fun getVendors() {
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.VENDORS,
            VendorFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (vendor in result.getOrNull().orEmpty()) {
                vendorRepository.insertVendor(vendor.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading vendor")
        }
    }

    private suspend fun getCoupons() {
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.COUPONS,
            CouponFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (coupon in result.getOrNull().orEmpty()) {
                couponRepository.insertCoupon(coupon.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading coupons")
        }
    }

    private suspend fun getCashCounter() {
        val result = syncDataWithFireStore.downloadAll(
            Constants.FirebaseDocument.CASH_COUNTER,
            CashCounterFireStoreDto::class.java
        )
        if (result.isSuccess) {
            for (cashCounter in result.getOrNull().orEmpty()) {
                cashCounterRepository.insertCashCounter(cashCounter.toDomain())
            }
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error while loading cashCounter")
        }
    }
}