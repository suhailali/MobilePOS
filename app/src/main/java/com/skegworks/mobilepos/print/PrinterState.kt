package com.skegworks.mobilepos.print

sealed interface PrinterState {

    data object Idle : PrinterState

    data object Scanning : PrinterState

    data object Connecting : PrinterState

    data object DiscoveringServices : PrinterState

    data object Ready : PrinterState

    data object Printing : PrinterState

    data object Printed : PrinterState

    data object Disconnected : PrinterState

    data class Error(
        val message: String
    ) : PrinterState
}