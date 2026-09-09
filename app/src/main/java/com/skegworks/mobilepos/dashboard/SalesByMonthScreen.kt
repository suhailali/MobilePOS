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
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.ui.component.SpacerSmall
import network.chaintech.cmpcharts.common.extensions.roundTwoDecimal

@Composable
fun SalesByMonthScreen(modifier: Modifier, viewModel: DashboardViewModel) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadSaleByMonth()
    }

    val list = state.salesByMonth ?: listOf()
    if (list.isEmpty()) {
        Text("No Data")
        return
    }

    Column(modifier = modifier) {
        Text("Sales by Month")
        SpacerLarge()
        LazyColumn() {
            itemsIndexed(
                items = list,
                key = { _, item -> item.month }) { index, item ->
                MonthlySaleRow(index, item)
            }
        }
    }
}

@Composable
fun MonthlySaleRow(index: Int, salesByMonth: SalesByMonth) {
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryFixed else MaterialTheme.colorScheme.primaryFixedDim

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Month:- ${salesByMonth.month}")
            Text(text = "No. Of Invoices:- ${salesByMonth.numberOfInvoices}")
        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Sale Quantity: ${salesByMonth.salesQuantity}")
                SpacerSmall()
                Text(text = "Sale Cost: ${salesByMonth.salesCost}")
                SpacerSmall()
                Text(text = "Sale Amount: ${salesByMonth.salesAmount}")
                SpacerSmall()
                Text(text = "Sale Profit: ${salesByMonth.salesProfit.roundTwoDecimal()}")
            }
            SpacerMedium()
            Column {
                Text(text = "Cash Discount: ${salesByMonth.cashDiscountGiven}")
                SpacerSmall()
                Text(text = "Offer Discount: ${salesByMonth.offerDiscountGiven}")
                SpacerSmall()
                Text(text = "Coupon Discount: ${salesByMonth.couponDiscountGiven}")
                SpacerSmall()
                Text(text = "GST: ${salesByMonth.outputGST.roundTwoDecimal()}")
            }
        }
        SpacerMedium()
        Text("Profit/Loss")
        Text(" ${salesByMonth.salesProfit.roundTwoDecimal() - 100000  - salesByMonth.cashDiscountGiven}")
    }
}