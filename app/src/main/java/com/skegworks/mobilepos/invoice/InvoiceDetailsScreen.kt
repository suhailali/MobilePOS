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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    var openCreditNoteConfirmationDialog by remember { mutableStateOf(false) }
    Column {
        SpacerLarge()
        state.selectedInvoice?.let {
            InvoiceListRow(index = 0, invoice = it) {
                //ignore
            }
        }
        SpacerLarge()
        state.invoiceItems?.let {
            InvoiceItemList(it)
        }
        SpacerMedium()
        Button(onClick = {
            openCreditNoteConfirmationDialog = true
        }) {
            Text("Add to Credit Note")
        }
    }

    if (openCreditNoteConfirmationDialog) {
        SimpleAlertDialog(
            onDismissRequest = { openCreditNoteConfirmationDialog = false },
            onConfirmation = {
                openCreditNoteConfirmationDialog = false

            },
            dialogTitle = "Delete Product",
            dialogText = "Are you sure you want to delete this product?",
            icon = Icons.Default.Info
        )
    }
}

@Composable
fun InvoiceItemList(list: List<InvoiceItem>) {
    LazyColumn {
        itemsIndexed(
            items = list,
            key = { _, item -> item.id }) { index, product ->
            InvoiceItemListRow(index, product) {

            }
        }
    }
}

@Composable
fun InvoiceItemListRow(index: Int, invoiceItem: InvoiceItem, onClick: () -> Unit) {
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.inversePrimary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(Dimens.MEDIUM_PADDING.dp)
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(invoiceItem.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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