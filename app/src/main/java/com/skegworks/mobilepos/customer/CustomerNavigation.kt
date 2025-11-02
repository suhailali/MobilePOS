package com.skegworks.mobilepos.customer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.product.AddProductScreen
import com.skegworks.mobilepos.product.ProductListScreen
import com.skegworks.mobilepos.product.ProductViewModel
import kotlinx.serialization.Serializable


@Serializable
object CustomerList

@Serializable
object AddCustomer

@Composable
fun CustomerScreenNavigation(
    viewModel: CustomerViewModel,
    modifier: Modifier,
    onCustomerSelected: (customer: Customer) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CustomerList
    ) {
        composable<CustomerList> {
            CustomerSearchScreen(
                modifier = modifier,
                viewModel = viewModel,
                onSelect = {
                    onCustomerSelected(it)
                }) {
                navController.navigate(AddCustomer)
            }
        }
        composable<AddCustomer> {
            AddCustomerScreen(modifier = modifier, viewModel = viewModel) {
                navController.popBackStack()
            }
        }

    }

}