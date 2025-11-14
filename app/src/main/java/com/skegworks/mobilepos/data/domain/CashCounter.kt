package com.skegworks.mobilepos.data.domain

data class CashCounter(
    val id:String,
    val date:String,
    val openingBalance:Double,
    var closingBalance:Double,
    var status: CashCounterStatus,
    val openedAt:Long,
    val openedBy:String,
    var closedAt:Long,
    var closedBy:String,
    var isSynced:Boolean
)

enum class CashCounterStatus(val value:String) {
    OPEN("open"),
    CLOSED("closed");

    companion object {
        fun fromStatus(status: String): CashCounterStatus? {
            return CashCounterStatus.entries.firstOrNull { it.value.equals(status, ignoreCase = true) }
        }
    }
}