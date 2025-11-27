package com.skegworks.mobilepos.product

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


@Serializable
object ProductList

@Serializable
object AddProduct

@Serializable
data class ProductDetail(val id: String)

@Composable
fun ProductScreenNavigation(
    viewModel: ProductViewModel,
    modifier: Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ProductList
    ) {
        composable<ProductList> {
            ProductListScreen(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToProductDetail = { id ->
                    navController.navigate(ProductDetail(id = id))
                }) {
                navController.navigate(AddProduct)
            }
        }
        composable<AddProduct> {
            AddProductScreen(modifier = modifier, viewModel = viewModel)
        }
        composable<ProductDetail> { backStackEntry ->
            val id: String? = backStackEntry.arguments?.getString("id")
            id?.let {
                ProductDetailScreen(modifier = modifier, viewModel = viewModel, productId = it)
            }
        }

    }

}