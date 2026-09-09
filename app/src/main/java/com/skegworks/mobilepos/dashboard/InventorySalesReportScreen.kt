package com.skegworks.mobilepos.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.ui.component.SpacerSmall

@Composable
fun InventorySalesReportScreen(
    modifier: Modifier,
    viewModel: DashboardViewModel,
    onSelect: (vendorId: String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadInventorySalesByVendor()
    }
    val list = state.inventorySalesByVendor ?: listOf()
    if (list.isEmpty()) {
        Text("No Data")
        return
    }
    Column(modifier = modifier) {
        Text("Inventory Sales by Vendor")
        state.totalInventorySales?.let {
            SpacerMedium()
            SaleRow(it)
            SpacerLarge()
        }
        LazyColumn {
            itemsIndexed(
                items = list,
                key = { _, item -> item.vendorName }) { index, item ->
                VendorSaleItem(item, index) { onSelect(it) }
            }
        }
    }
}

@Composable
fun VendorSaleItem(inventorySaleByVendor: InventorySaleByVendor, index: Int, onSelect: (vendorId: String) -> Unit) {
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryFixed else MaterialTheme.colorScheme.primaryFixedDim
    Column(modifier = Modifier.background(backgroundColor).padding(12.dp).clickable{
        onSelect(inventorySaleByVendor.vendorId)
    }) {
        SaleRow(inventorySaleByVendor)
    }
}

@Composable
fun SaleRow(inventorySaleByVendor: InventorySaleByVendor) {
    Text(text = "Vendor:- ${inventorySaleByVendor.vendorName}",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
    )
    Row(
        modifier = Modifier.padding(10.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Stock: ${inventorySaleByVendor.stockQuantity}")
            SpacerSmall()
            Text(text = "Unsold Cost: ${inventorySaleByVendor.unsoldCost}")
            SpacerSmall()
            Text(text = "Unsold Final: ${inventorySaleByVendor.unsoldAmount}")
        }
        SpacerMedium()
        Column {
            Text(text = "Sold: ${inventorySaleByVendor.soldQuantity}")
            SpacerSmall()
            Text(text = "Sales: ${inventorySaleByVendor.salesAmount}")
            SpacerSmall()
            Text(text = "Profit: ${inventorySaleByVendor.profit}")
        }
    }
}