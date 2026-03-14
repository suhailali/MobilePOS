package com.skegworks.mobilepos.customer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skegworks.mobilepos.data.domain.Customer
import kotlinx.serialization.Serializable


@Serializable
object CustomerList

@Serializable
object AddCustomer
@Serializable
class CustomerDetails(val customerId: String)

@Composable
fun CustomerScreenNavigation(
    viewModel: CustomerViewModel,
    modifier: Modifier,
    isFromLandingScreen: Boolean,
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
                    if (isFromLandingScreen) {
                        onCustomerSelected(it)
                    } else {
                        navController.navigate(CustomerDetails(customerId = it.id))
                    }
                }) {
                navController.navigate(AddCustomer)
            }
        }
        composable<AddCustomer> {
            AddCustomerScreen(modifier = modifier, viewModel = viewModel) {
                navController.popBackStack()
            }
        }
        composable<CustomerDetails> { backStackEntry ->
            val customerId: String? = backStackEntry.arguments?.getString("customerId")
            customerId?.let {
                CustomerDetailsScreen(modifier = modifier, viewModel = viewModel, customerId = it) {
                    navController.popBackStack()
                }
            }
        }
    }
}
