package com.skegworks.mobilepos.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.ui.component.SpacerSmall

@Composable
fun DashboardScreen(modifier: Modifier, viewModel: DashboardViewModel) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }
    state.dashboardValue?.let {
        Column(modifier = Modifier.fillMaxWidth()) {
            SpacerLarge()
            SpacerLarge()
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Number of Products", it.totalNoOfProducts.toString())
                SpacerSmall()
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Quantity of Products", it.totalQuantityOfProducts.toString())
            }
            SpacerMedium()
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                HeaderAndLabel( modifier = Modifier.weight(1f),"Out of Stock Products", it.outOfStockProducts.toString())
                SpacerSmall()
                HeaderAndLabel( modifier = Modifier.weight(1f),"Dead Stock Products", it.totalInactiveProduct.toString())
            }
            SpacerMedium()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Cost of Products in Stock", it.totalCostOfProductsInStockWithOutGST.toString())
                SpacerSmall()
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Input GST of Products in Stock", it.totalInputGSTOfProductsInStock.toString())
            }
            SpacerMedium()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Cost of All Products", it.totalCostOfProductsWithOutGST.toString())
                SpacerSmall()
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Input GST of All Products", it.totalInputGSTOfProducts.toString())
            }
            SpacerMedium()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Price of Products in Stock", it.totalPriceOfProductsInStockWithOutGST.toString())
                SpacerSmall()
                HeaderAndLabel( modifier = Modifier.weight(1f),"Total Output GST of Products in Stock", it.totalOutputGSTOfProductsInStock.toString())
            }
        }
    }
}

@Composable
fun HeaderAndLabel(modifier: Modifier, header: String, label: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = header, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(text = label)
    }
}