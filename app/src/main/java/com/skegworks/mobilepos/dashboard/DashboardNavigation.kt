package com.skegworks.mobilepos.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skegworks.mobilepos.product.ProductDetail
import com.skegworks.mobilepos.product.ProductDetailScreen
import com.skegworks.mobilepos.product.ProductViewModel
import kotlinx.serialization.Serializable

@Serializable
object InventorySalesReport

@Serializable
object Dashboard

@Serializable
class ProductList(val vendorId: String)

@Serializable
object SalesByMonthScreen

@Composable
fun DashboardScreenNavigation(
    dashboardViewModel: DashboardViewModel,
    productViewModel: ProductViewModel,
    modifier: Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Dashboard
    ) {
        composable<Dashboard> {
            DashboardScreen(modifier = modifier, viewModel = dashboardViewModel, navigateToMonthlySales = {
                navController.navigate(SalesByMonthScreen)
            }) {
                navController.navigate(InventorySalesReport)
            }
        }
        composable<InventorySalesReport> {
            InventorySalesReportScreen(
                modifier = modifier, viewModel = dashboardViewModel,
                onSelect = {
                    navController.navigate(ProductList(it))
                })
        }
        composable<ProductList> { backStackEntry ->
            val vendorId: String? = backStackEntry.arguments?.getString("vendorId")
            vendorId?.let {
                ProductListByVendor(modifier = modifier, viewModel = dashboardViewModel, vendorId = it) { productId ->
                    navController.navigate(ProductDetail(productId))
                }
            }
        }
        composable<SalesByMonthScreen> {
            SalesByMonthScreen(modifier = modifier, viewModel = dashboardViewModel)
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