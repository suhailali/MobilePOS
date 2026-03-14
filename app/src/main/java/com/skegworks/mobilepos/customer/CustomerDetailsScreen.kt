package com.skegworks.mobilepos.customer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CustomerDetailsScreen(
    modifier: Modifier,
    viewModel: CustomerViewModel,
    customerId: String,
    onBack: () -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(DestinationCustomerScreen.DETAILS.ordinal) }

    // Intercept back button to always exit this screen
    BackHandler {
        onBack()
    }

    LaunchedEffect(Unit) {
        viewModel.handleCustomerDetailsIntent(CustomerDetailsIntent.SetCustomerDetail(customerId))
    }

    Scaffold(modifier = modifier) { contentPadding ->
        Column(modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                DestinationCustomerScreen.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(
                                text = destination.label,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

            // Use a simple when block instead of a NavHost to avoid nested backstack issues
            val contentModifier = Modifier
                .fillMaxSize()
                .weight(1f)
            when (DestinationCustomerScreen.entries[selectedTabIndex]) {
                DestinationCustomerScreen.DETAILS -> {
                    AddCustomerScreen(
                        modifier = contentModifier,
                        viewModel = viewModel,
                        isUpdate = true
                    ) {
                        // Handle success if needed
                    }
                }

                DestinationCustomerScreen.COUPONS -> {
                    CustomerCouponsScreen(modifier = contentModifier, viewModel = viewModel)
                }

                DestinationCustomerScreen.INVOICES -> {
                    CustomerInvoiceScreen(modifier = contentModifier, viewModel = viewModel)
                }
            }
        }
    }
}

enum class DestinationCustomerScreen(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    DETAILS("details", "Details", Icons.Default.FileOpen, "Details"),
    COUPONS("coupons", "Coupons", Icons.Default.Tag, "Coupons"),
    INVOICES("invoices", "Invoices", Icons.Default.Inbox, "Invoices"),
}
