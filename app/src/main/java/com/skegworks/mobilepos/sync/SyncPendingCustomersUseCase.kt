package com.skegworks.mobilepos.sync

interface SyncPendingCustomersUseCase {
    suspend operator fun invoke()
}