package com.skegworks.mobilepos.invoice

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun InvoiceListScreen(modifier: Modifier, viewModel: InvoiceViewModel, onInvoiceSelected:(id:String) -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.handleIntent(InvoiceIntent.LoadInvoices)
    }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MEDIUM_PADDING.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Invoices", style = MaterialTheme.typography.titleLarge, fontWeight = Bold)
            TextButton(onClick = {
                viewModel.handleIntent(InvoiceIntent.SyncInvoices)
            }) {
                Text("Sync Invoices")
                if (state.unsyncedInvoices > 0) {
                    Text("" + state.unsyncedInvoices)
                }
            }
        }

        if (state.isLoading.not()) {
            InvoiceList(state.invoices) { id ->
                onInvoiceSelected(id)
            }
        } else {
            SpacerLarge()
            SpacerLarge()
            SpacerLarge()
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Loading Invoices...")
            }
        }
    }
}

@Composable
fun InvoiceList(list: List<Invoice>, onClick: (id: String) -> Unit) {
    LazyColumn {
        itemsIndexed(
            items = list,
            key = { _, item -> item.id }) { index, invoice ->
            InvoiceListRow(index, invoice) {
                onClick(invoice.id)
            }
        }
    }
}

@Composable
fun InvoiceListRow(index: Int, invoice: Invoice, onClick: () -> Unit) {
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.inversePrimary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(Dimens.MEDIUM_PADDING.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Invoice Number: ${invoice.invoiceNumber}",
                fontSize = 16.sp,
                fontWeight = Bold
            )
            if (invoice.isCreditNote) {
                Text("Credit Note", fontSize = 16.sp, fontWeight = Bold)
            }
        }
        Text(
            "Invoice Date: ${invoice.invoiceDate}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Customer: ${invoice.customer.name}", modifier = Modifier.weight(1f))
            Text("Phone: ${invoice.customer.phone}", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Final Amount: ${invoice.finalPrice}", fontSize = 16.sp,
                fontWeight = Bold, modifier = Modifier.weight(1f)
            )
            Text("Products: ${invoice.items.size}", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Cash Discount: ${invoice.cashDiscount}", modifier = Modifier.weight(1f))
            Text("Invoice State: ${invoice.invoiceState}", modifier = Modifier.weight(1f))
        }
    }
}