package com.skegworks.mobilepos.vendors

data class VendorDetailsState(
    val textStateName: String = "",
    val textStateAddress: String = "",
    val textStateCity: String = "",
    val textStateState: String = "",
    val textStateCountry: String = "",
    val textStatePhone: String = "",
    val textStateEmail: String = "",
    val textStateZipCode: String = "",
    val textStateGST: String = "",
    val textStateGSTPercentage: String = "",
    val textStateCurrency: String = "",
    val isSaved: Boolean = false
)
