package com.skegworks.mobilepos.cashcounter

sealed class CashCounterIntent{
    object UpdateCashCounter: CashCounterIntent()
    data class UpdateCash(val cash:Double): CashCounterIntent()
}
