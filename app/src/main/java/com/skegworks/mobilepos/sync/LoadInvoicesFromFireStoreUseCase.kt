package com.skegworks.mobilepos.sync

interface LoadInvoicesFromFireStoreUseCase {
    suspend operator fun invoke(onCompletion: () -> Unit)
}