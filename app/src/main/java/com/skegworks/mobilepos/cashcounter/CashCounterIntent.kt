package com.skegworks.mobilepos.cashcounter

sealed class CashCounterIntent{
    object OpenCashCounter: CashCounterIntent()
    object CloseCashCounter: CashCounterIntent()
    data class UpdateCash(val cash:Double): CashCounterIntent()
}
