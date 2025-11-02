package com.skegworks.mobilepos.data.domain

data class CashCounter(
    val id:String,
    val date:String,
    val balance:Double,
    val status: CashCounterStatus,
    val createdAt:Long,
    val createdBy:String,
    var isSynced:Boolean
)

enum class CashCounterStatus(val value:String) {
    OPEN("open"),
    CLOSED("closed");

    companion object {
        fun fromRole(status: String): CashCounterStatus? {
            return CashCounterStatus.entries.firstOrNull { it.value.equals(status, ignoreCase = true) }
        }
    }
}