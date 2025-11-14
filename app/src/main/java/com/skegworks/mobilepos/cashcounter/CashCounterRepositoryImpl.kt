package com.skegworks.mobilepos.cashcounter

import com.skegworks.mobilepos.data.domain.CashCounter
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import javax.inject.Inject

class CashCounterRepositoryImpl @Inject constructor(private val cashCounterDao: CashCounterDao) :
    CashCounterRepository {
    override suspend fun insertCashCounter(cashCounter: CashCounter) {
        cashCounterDao.insertCashCounter(cashCounter.toEntity())
    }

    override suspend fun updateCashCounter(cashCounter: CashCounter) {
        cashCounterDao.updateCashCounter(cashCounter.toEntity())
    }

    override suspend fun getLatestCashCounter(): CashCounter? {
        val cashCounter = cashCounterDao.getLatestCashCounter()
        if (cashCounter == null) {
            return null
        }
        return cashCounter.toDomain()
    }

    override suspend fun getAllUnsyncedCashCounter(): List<CashCounter> {
        return cashCounterDao.getAllUnSyncedCashCounters().map {
            it.toDomain()
        }
    }
}