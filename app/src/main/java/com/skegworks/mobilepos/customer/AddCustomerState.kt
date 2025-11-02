package com.skegworks.mobilepos.customer

data class AddCustomerState(

    val textStateName: String = "",
    val textStatePhone: String = "",
    val textStateAddress: String = "",
    val textStateEmail: String = "",
    val textStateIsActive: Boolean = true,
    val textStateCreatedAt: Long = System.currentTimeMillis(),
    val textStateUpdatedAt: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false,
    val error: String? = null
) {
    fun clearState(): AddCustomerState {
        return AddCustomerState()
    }
}
