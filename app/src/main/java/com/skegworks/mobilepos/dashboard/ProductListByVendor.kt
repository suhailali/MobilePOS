package com.skegworks.mobilepos.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.skegworks.mobilepos.product.ProductListRow

@Composable
fun ProductListByVendor(modifier: Modifier,
                        viewModel: DashboardViewModel,
                        vendorId: String,
                        onSelect: (String) -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadProductForVendor(vendorId)
    }

    Column(modifier = modifier) {
        LazyColumn {
            itemsIndexed(
                items = state.productList,
                key = { _, item -> item.id }) { index, product ->
                ProductListRow(index, product) {
                    onSelect(product.id)
                }
            }
        }
    }
}