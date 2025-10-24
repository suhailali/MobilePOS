package com.skegworks.mobilepos.vendors

data class VendorDetailsState(
    val textStateName: String = "",
    val textStateAddress: String = "",
    val textStateCity: String = "",
    val textStateState: String = "",
    val textStateCountry: String = "India",
    val textStatePhone: String = "",
    val textStateEmail: String = "",
    val textStateZipCode: String = "",
    val textStateGST: String = "",
    val textStateGSTPercentage: String = "",
    val textStateCurrency: String = "Rupees",
    val isSaved: Boolean = false
) {
    fun clearState(): VendorDetailsState {
        return VendorDetailsState()
    }
}
