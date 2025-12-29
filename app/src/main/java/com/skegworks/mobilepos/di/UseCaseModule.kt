package com.skegworks.mobilepos.di

import com.skegworks.mobilepos.appsettings.AppSettingsRepository
import com.skegworks.mobilepos.appsettings.GenerateNewProductCounterUseCase
import com.skegworks.mobilepos.appsettings.GenerateNewProductCounterUseCaseImpl
import com.skegworks.mobilepos.appsettings.InitialiseAppSettingsUseCase
import com.skegworks.mobilepos.appsettings.InitialiseAppSettingsUseCaseImpl
import com.skegworks.mobilepos.appsettings.SyncAppSettingsUseCase
import com.skegworks.mobilepos.appsettings.SyncAppSettingsUseCaseImpl
import com.skegworks.mobilepos.appsettings.UpdateProductCounterUseCase
import com.skegworks.mobilepos.appsettings.UpdateProductCounterUseCaseImpl
import com.skegworks.mobilepos.business.BusinessRepository
import com.skegworks.mobilepos.coupon.CouponRepository
import com.skegworks.mobilepos.creditnote.CreditNoteRepository
import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.customer.FetchCustomerUseCase
import com.skegworks.mobilepos.customer.FetchCustomerUseCaseImpl
import com.skegworks.mobilepos.dashboard.GetDashboardDataUseCase
import com.skegworks.mobilepos.dashboard.GetDashboardDataUseCaseImpl
import com.skegworks.mobilepos.invoice.GenerateInvoicePdfUseCase
import com.skegworks.mobilepos.invoice.GenerateInvoicePdfUseCaseImpl
import com.skegworks.mobilepos.invoice.GenerateNewCreditNoteUseCase
import com.skegworks.mobilepos.invoice.GenerateNewCreditNoteUseCaseImpl
import com.skegworks.mobilepos.invoice.GenerateNewInvoiceNumberUseCase
import com.skegworks.mobilepos.invoice.GenerateNewInvoiceNumberUseCaseImpl
import com.skegworks.mobilepos.invoice.GetInvoiceDetailUseCase
import com.skegworks.mobilepos.invoice.GetInvoiceDetailUseCaseImpl
import com.skegworks.mobilepos.invoice.GetInvoicesUseCase
import com.skegworks.mobilepos.invoice.GetInvoicesUseCaseImpl
import com.skegworks.mobilepos.invoice.InvoiceRepository
import com.skegworks.mobilepos.invoice.SyncInvoiceUseCase
import com.skegworks.mobilepos.invoice.SyncInvoiceUseCaseImpl
import com.skegworks.mobilepos.invoice.UpdateInventoryAfterCreditNoteUseCase
import com.skegworks.mobilepos.invoice.UpdateInventoryAfterCreditNoteUseCaseImpl
import com.skegworks.mobilepos.invoice.UpdateInvoiceNumberUseCase
import com.skegworks.mobilepos.invoice.UpdateInvoiceNumberUseCaseImpl
import com.skegworks.mobilepos.pos.GetCouponFromBarCodeUseCaseImpl
import com.skegworks.mobilepos.pos.GetCouponFromBarcodeUseCase
import com.skegworks.mobilepos.pos.GetProductFromBarCodeUseCaseImpl
import com.skegworks.mobilepos.pos.GetProductFromBarcodeUseCase
import com.skegworks.mobilepos.pos.UpdateInventoryAfterSaleUseCase
import com.skegworks.mobilepos.pos.UpdateInventoryAfterSaleUseCaseImpl
import com.skegworks.mobilepos.product.CalculateProductPriceUseCase
import com.skegworks.mobilepos.product.CalculateProductPriceUseCaseImpl
import com.skegworks.mobilepos.product.ProductRepository
import com.skegworks.mobilepos.product.SyncProductUseCase
import com.skegworks.mobilepos.sync.LoadInvoicesFromFireStoreUseCase
import com.skegworks.mobilepos.sync.LoadInvoicesFromFireStoreUseCaseImpl
import com.skegworks.mobilepos.sync.SyncData
import com.skegworks.mobilepos.utils.DateUtility
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
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

    @Provides
    fun provideSyncInvoiceUseCase(repository: InvoiceRepository): SyncInvoiceUseCase {
        return SyncInvoiceUseCaseImpl(repository)
    }

    @Provides
    fun providesFetchCustomerUseCase(repository: CustomerRepository): FetchCustomerUseCase {
        return FetchCustomerUseCaseImpl(repository)
    }

    @Provides
    fun providesGenerateNewInvoiceNumberUseCase(
        appSettingsRepository: AppSettingsRepository,
        dateUtility: DateUtility
    ): GenerateNewInvoiceNumberUseCase {
        return GenerateNewInvoiceNumberUseCaseImpl(appSettingsRepository, dateUtility)
    }

    @Provides
    fun providesUpdateInvoiceNumberUseCase(
        appSettingsRepository: AppSettingsRepository,
        dateUtility: DateUtility
    ): UpdateInvoiceNumberUseCase {
        return UpdateInvoiceNumberUseCaseImpl(appSettingsRepository, dateUtility)
    }

    @Provides
    fun providesInitialiseAppSettingsUseCase(
        appSettingsRepository: AppSettingsRepository,
        dateUtility: DateUtility,
        userPreferenceHandler: UserPreferenceHandler
    ): InitialiseAppSettingsUseCase {
        return InitialiseAppSettingsUseCaseImpl(
            appSettingsRepository,
            dateUtility,
            userPreferenceHandler
        )
    }

    @Provides
    fun provideGetCouponFromBarcodeUseCase(couponRepository: CouponRepository): GetCouponFromBarcodeUseCase {
        return GetCouponFromBarCodeUseCaseImpl(couponRepository)
    }

    @Provides
    fun providePriceCalculationUseCase(): CalculateProductPriceUseCase {
        return CalculateProductPriceUseCaseImpl()
    }

    @Provides
    fun provideSyncAppSettingsUseCase(
        appSettingsRepository: AppSettingsRepository
    ): SyncAppSettingsUseCase {
        return SyncAppSettingsUseCaseImpl(appSettingsRepository)
    }

    @Provides
    fun providesUpdateInventoryAfterSaleUseCase(
        productRepository: ProductRepository,
        syncProductUseCase: SyncProductUseCase
    ): UpdateInventoryAfterSaleUseCase {
        return UpdateInventoryAfterSaleUseCaseImpl(productRepository, syncProductUseCase)
    }

    @Provides
    fun providesGetDashboardDataUseCase(
        productRepository: ProductRepository,
        customerRepository: CustomerRepository,
        invoiceRepository: InvoiceRepository,
    ): GetDashboardDataUseCase {
        return GetDashboardDataUseCaseImpl(
            productRepository,
            customerRepository,
            invoiceRepository
        )
    }

    @Provides
    fun providesGenerateProductCounterUseCase(
        appSettingsRepository: AppSettingsRepository
    ): GenerateNewProductCounterUseCase {
        return GenerateNewProductCounterUseCaseImpl(appSettingsRepository)
    }

    @Provides
    fun providesUpdateProductCounterUseCase(
        appSettingsRepository: AppSettingsRepository
    ): UpdateProductCounterUseCase {
        return UpdateProductCounterUseCaseImpl(appSettingsRepository)
    }

    @Provides
    fun providesGetInvoicesUseCase(
        invoiceRepository: InvoiceRepository
    ): GetInvoicesUseCase {
        return GetInvoicesUseCaseImpl(invoiceRepository)
    }

    @Provides
    fun providesLoadInvoicesFromFireStoreUseCase(
        syncData: SyncData,
        invoiceRepository: InvoiceRepository,
        customerRepository: CustomerRepository,
        businessRepository: BusinessRepository,
        couponRepository: CouponRepository
    ): LoadInvoicesFromFireStoreUseCase {
        return LoadInvoicesFromFireStoreUseCaseImpl(
            syncData,
            invoiceRepository,
            customerRepository,
            businessRepository,
            couponRepository
        )
    }

    @Provides
    fun providesGetInvoiceDetailsUseCase(
        invoiceRepository: InvoiceRepository
    ): GetInvoiceDetailUseCase {
        return GetInvoiceDetailUseCaseImpl(invoiceRepository)
    }

    @Provides
    fun providesGenerateCreditNoteUseCase(
        dateUtility: DateUtility,
        uuidGenerator: UUIDGenerator,
        creditNoteRepository: CreditNoteRepository
    ): GenerateNewCreditNoteUseCase {
        return GenerateNewCreditNoteUseCaseImpl(dateUtility, uuidGenerator, creditNoteRepository)
    }

    @Provides
    fun providesUpdateInventoryAfterCreditNoteUseCase(
        productRepository: ProductRepository,
        syncProductUseCase: SyncProductUseCase
    ): UpdateInventoryAfterCreditNoteUseCase {
        return UpdateInventoryAfterCreditNoteUseCaseImpl(productRepository, syncProductUseCase)
    }
}