package com.skegworks.mobilepos.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.room.Index
import com.skegworks.mobilepos.ui.component.SpacerLarge

@Composable
fun TopCustomersScreen(modifier: Modifier, viewModel: DashboardViewModel) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadTopCustomers()
    }
    val list = state.topCustomers
    if (list.isEmpty()) {
        return
    }
    Column {
        SpacerLarge()
        LazyColumn {
            itemsIndexed(items = list, key = { index, item -> item.id }) { index, item ->
                CustomerRow(index = index, topCustomer = item)
            }
        }
    }
}

@Composable
fun CustomerRow(index: Int, topCustomer: TopCustomer) {
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryFixed else MaterialTheme.colorScheme.primaryFixedDim
    Column(modifier = Modifier.background(backgroundColor).padding(12.dp)) {
        CustomerItem(topCustomer)
    }
}

@Composable
fun CustomerItem(topCustomer: TopCustomer) {
    Column {
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = topCustomer.name)
            Text(text = topCustomer.phone)
        }

        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total Spend: ${topCustomer.totalSpend}")
            Text(text = "Total Visits: ${topCustomer.totalVisits}")
        }

        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total Discount Received: ${topCustomer.totalDiscountReceived}")
            Text(text = "Max Spend: ${topCustomer.maxSpend}")
        }
    }
}