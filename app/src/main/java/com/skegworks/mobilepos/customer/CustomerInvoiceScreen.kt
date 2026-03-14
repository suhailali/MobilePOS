package com.skegworks.mobilepos.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.skegworks.mobilepos.invoice.InvoiceList

@Composable
fun CustomerInvoiceScreen(modifier: Modifier, viewModel: CustomerViewModel) {
    val state by viewModel.stateDetails.collectAsState()
    Column {
        InvoiceList(state.invoiceList) {

        }
    }
}