package com.skegworks.mobilepos.appsettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppSettingsScreen(modifier: Modifier, viewModel: AppSettingsViewModel) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchAppSettings()
    }
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        state.value.appSettings?.let {
            Text("Invoice Counter : ${it.invoiceCounter}")
            Text("Invoice Year : ${it.invoiceYear}")
            Text("Product Counter : ${it.productSkuCounter}")
        }
    }
}
