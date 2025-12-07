package com.skegworks.mobilepos.sync

interface LoadInvoicesFromFireStoreUseCase {
    operator fun invoke(onCompletion: () -> Unit)
}