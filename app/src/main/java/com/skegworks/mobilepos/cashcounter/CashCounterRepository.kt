package com.skegworks.mobilepos.cashcounter

import com.skegworks.mobilepos.data.domain.CashCounter

interface CashCounterRepository {
    suspend fun insertCashCounter(cashCounter: CashCounter)
    suspend fun updateCashCounter(cashCounter: CashCounter)
    suspend fun getLatestCashCounter(): CashCounter?
}