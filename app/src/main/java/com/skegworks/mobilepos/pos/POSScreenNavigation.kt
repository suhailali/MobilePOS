package com.skegworks.mobilepos.pos

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@Serializable
object AddItem

@Serializable
object SearchProduct

@Serializable
object AddCoupon

@Serializable
object ApplyDiscount

@Serializable
object POSScreen

@OptIn(ExperimentalGetImage::class)
@Composable
fun POSScreenNavigation(
    viewmodel: POSViewModel,
    modifier: Modifier
) {
    val navController = rememberNavController()
    val posNavigator = POSNavigator(navController)

    NavHost(
        navController = navController,
        startDestination = POSScreen
    ) {
        composable<POSScreen> {
            POSScreen(modifier = modifier, viewModel = viewmodel, posNavigator)
        }

        composable<SearchProduct> {  }

        composable<AddItem> {
            BarcodeScannerScreen(modifier = modifier, viewModel = viewmodel) {
                navController.popBackStack()
            }
        }
    }

}

class POSNavigator(private val navController: NavController) {
    fun navigateToAddItem() = navController.navigate(AddItem)
    fun navigateToPOSScreen() = navController.navigate(POSScreen)
}