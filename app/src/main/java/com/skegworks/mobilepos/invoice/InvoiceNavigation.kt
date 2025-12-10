package com.skegworks.mobilepos.invoice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object InvoiceList

@Serializable
data class InvoiceDetail(val id: String)

@Composable
fun InvoiceScreenNavigation(
    viewModel: InvoiceViewModel,
    modifier: Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = InvoiceList
    ) {
        composable<InvoiceList> {
            InvoiceListScreen(
                modifier = modifier,
                viewModel = viewModel,
                onInvoiceSelected = { invoiceId ->
                    navController.navigate(InvoiceDetail(id = invoiceId))
                }
            )
        }
        composable<InvoiceDetail> { backStackEntry ->
            val id: String? = backStackEntry.arguments?.getString("id")
            id?.let {
                InvoiceDetailsScreen(modifier = modifier, viewModel = viewModel, invoiceId = it) {
                    navController.popBackStack()
                }
            }
        }
    }
}