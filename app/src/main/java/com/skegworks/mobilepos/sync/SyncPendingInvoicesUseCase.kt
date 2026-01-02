package com.skegworks.mobilepos.sync

interface SyncPendingInvoicesUseCase {
    suspend operator fun invoke()
}