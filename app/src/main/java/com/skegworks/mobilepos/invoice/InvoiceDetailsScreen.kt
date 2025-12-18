package com.skegworks.mobilepos.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.ui.component.SimpleAlertDialog
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun InvoiceDetailsScreen(modifier: Modifier, viewModel: InvoiceViewModel, invoiceId: String, navigateBack: () -> Unit) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(InvoiceIntent.SelectInvoice(invoiceId))
    }
    var openCreditNoteConfirmationDialog by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SpacerLarge()
        Text("Invoice", style = MaterialTheme.typography.titleLarge, fontWeight = Bold)
        Box(Modifier.background(MaterialTheme.colorScheme.onPrimary).padding(2.dp)) {
            state.selectedInvoice?.let {
                InvoiceListRow(index = 1, invoice = it) {
                    //ignore
                }
            }
        }
        SpacerLarge()
        Text("Invoice Items", style = MaterialTheme.typography.titleLarge, fontWeight = Bold)
        state.selectedInvoiceItems?.let {
            InvoiceItemList(it) { invoiceItem, checked ->
                viewModel.handleIntent(InvoiceIntent.CreditNoteInvoiceItem(invoiceItem, checked))
            }
        }
        SpacerMedium()
        Button(
            enabled = !state.creditNoteInvoiceItems.isNullOrEmpty(),
            onClick = {
            openCreditNoteConfirmationDialog = true
        }) {
            Text("Mark Product Return")
        }
    }

    if (openCreditNoteConfirmationDialog) {
        SimpleAlertDialog(
            onDismissRequest = { openCreditNoteConfirmationDialog = false },
            onConfirmation = {
                openCreditNoteConfirmationDialog = false
                viewModel.handleIntent(InvoiceIntent.ConfirmCreditNote)
            },
            dialogTitle = "Return Product",
            dialogText = "Are you sure you want to return product(s)?",
            icon = Icons.Default.Info
        )
    }
}

@Composable
fun InvoiceItemList(list: List<InvoiceItem>, onItemSelected :(InvoiceItem, Boolean) -> Unit) {
    LazyColumn {
        itemsIndexed(
            items = list,
            key = { _, item -> item.id }) { index, invoiceItem ->
            InvoiceItemListRow(index, invoiceItem) {
                onItemSelected(invoiceItem, it)
            }
        }
    }
}

@Composable
fun InvoiceItemListRow(index: Int, invoiceItem: InvoiceItem, onLongClick: (Boolean) -> Unit) {
    var checked by remember { mutableStateOf(false) }
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.inversePrimary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(Dimens.MEDIUM_PADDING.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    checked = !checked
                    onLongClick(checked)
                }
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(invoiceItem.title, fontSize = 16.sp, fontWeight = Bold)
            if (checked) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it}
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Rate: ${invoiceItem.salePriceWithoutDiscount}", modifier = Modifier.weight(1f))
            Text("Quantity: ${invoiceItem.quantity}", modifier = Modifier.weight(1f))
        }
        Text("Barcode: ${invoiceItem.sku}")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Color: ${invoiceItem.color}", modifier = Modifier.weight(1f))
            Text("Size: ${invoiceItem.size}", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Discount %: ${invoiceItem.discountPercentage}", modifier = Modifier.weight(1f))
            Text("Final Price: ${invoiceItem.finalRoundedOffPrice}", modifier = Modifier.weight(1f))
        }
    }
}