package com.skegworks.mobilepos.pos

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.skegworks.mobilepos.ui.component.LongPressAlertDialog
import kotlinx.serialization.Serializable


@Serializable
data class ScanCode(val scanType: BarcodeScanType)

@Serializable
object SearchProduct

@Serializable
object AddCoupon

@Serializable
object ApplyDiscount

@Serializable
object POSScreen

@Serializable
object AddCustomer

@OptIn(ExperimentalGetImage::class)
@Composable
fun POSScreenNavigation(
    viewmodel: POSViewModel,
    modifier: Modifier,
    finishActivity: () -> Unit
) {
    val navController = rememberNavController()
    val posNavigator = POSNavigator(navController)
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            showExitDialog = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = POSScreen
    ) {
        composable<POSScreen> {
            POSScreen(modifier = modifier, viewModel = viewmodel, posNavigator)
        }

        composable<SearchProduct> {

        }

        composable<ScanCode> { backStackEntry ->
            val scanCode: ScanCode = backStackEntry.toRoute()
            BarcodeScannerScreen(
                modifier = modifier,
                viewModel = viewmodel,
                scanCode = scanCode
            ) {
                navController.popBackStack()
            }
        }
    }

    if (showExitDialog) {
        LongPressAlertDialog(
            title = "Exit Billing?",
            description = "Hold the button below for 5 seconds to exit POS",
            onDismiss = {
            showExitDialog = false
        }) {
            showExitDialog = false
            finishActivity()
        }
    }
}

class POSNavigator(private val navController: NavController) {
    fun navigateToAddItem(scanFor: BarcodeScanType) =
        navController.navigate(ScanCode(scanType = scanFor))

    fun navigateToPOSScreen() = navController.navigate(POSScreen)
    fun navigateToCustomerScreen() = navController.navigate(AddCustomer)
}