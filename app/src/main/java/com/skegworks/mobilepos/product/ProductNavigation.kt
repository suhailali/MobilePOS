package com.skegworks.mobilepos.product

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@Serializable
object ProductList

@Serializable
object AddProduct

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
            ProductListScreen(modifier = modifier, viewModel = viewModel) {
                navController.navigate(AddProduct)
            }
        }
        composable<AddProduct> {
            AddProductScreen(modifier = modifier, viewModel = viewModel)
        }

    }

}