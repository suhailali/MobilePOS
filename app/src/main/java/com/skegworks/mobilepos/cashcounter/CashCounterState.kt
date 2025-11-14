package com.skegworks.mobilepos.cashcounter

import com.skegworks.mobilepos.data.domain.CashCounter
import com.skegworks.mobilepos.data.domain.CashCounterStatus

data class CashCounterState(
    val balance:Double = 0.0,
    val state: CashCounterStatus = CashCounterStatus.CLOSED,
    val textStateCashInCounter:Double = 0.0,
    val isLoading: Boolean = false,
    val isSynced: Boolean = false,
    val error: String? = null,
    val buttonText: String = "Open Cash Counter for Today",
    val cashCounterAction: CashCounterAction = CashCounterAction.ACTION_CLOSE,
    val buttonEnabled: Boolean = false,
    val previousCashCounter: CashCounter? = null
)