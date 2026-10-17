package com.skegworks.mobilepos.invoice

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skegworks.mobilepos.product.ProductDetail
import com.skegworks.mobilepos.product.ProductDetailScreen
import com.skegworks.mobilepos.product.ProductViewModel
import kotlinx.serialization.Serializable
import kotlin.getValue

@Serializable
object InvoiceList

@Serializable
data class InvoiceDetail(val id: String)

@Composable
fun InvoiceScreenNavigation(
    viewModel: InvoiceViewModel,
    productViewModel: ProductViewModel,
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
                InvoiceDetailsScreen(modifier = modifier, viewModel = viewModel, invoiceId = it, { productId ->
                    navController.navigate(ProductDetail(id = productId))
                }) {
                    navController.popBackStack()
                }
            }
        }
        composable<ProductDetail> { backStackEntry ->
            val id: String? = backStackEntry.arguments?.getString("id")
            id?.let {
                ProductDetailScreen(modifier = modifier, viewModel = productViewModel, productId = it) {
                    navController.popBackStack()
                }
            }
        }
    }
}