package com.skegworks.mobilepos.invoice

import android.widget.Toast
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.ui.component.AlertDialogWithTextInput
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.ui.component.TextFieldBottomSheet
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun InvoiceDetailsScreen(modifier: Modifier, viewModel: InvoiceViewModel, invoiceId: String, navigateBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.handleIntent(InvoiceIntent.SelectInvoice(invoiceId))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                InvoiceEvents.NAVIGATE_BACK -> navigateBack()
            }
        }
    }

    LaunchedEffect(state.pdfGenerated) {
        if (state.pdfGenerated) {
            state.invoicePDF?.let {
                viewModel.printPdf(context, it, state.selectedInvoice?.invoiceNumber ?: "Invoice")
            }
        }
    }

    var openCreditNoteConfirmationDialog by remember { mutableStateOf(false) }
    var isQuantitySheetOpen by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SpacerLarge()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MEDIUM_PADDING.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Invoice", style = MaterialTheme.typography.titleLarge, fontWeight = Bold)
            TextButton(onClick = {
                viewModel.handleIntent(InvoiceIntent.PrintInvoice)
            }) {
                Text("Print Invoice")
            }
        }

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
                if (!checked) {
                    viewModel.handleIntent(
                        InvoiceIntent.CreditNoteInvoiceItem(
                            invoiceItem,
                            invoiceItem.quantity,
                            false
                        )
                    )
                }
                else {
                    if (invoiceItem.quantity < 2) {
                        viewModel.handleIntent(
                            InvoiceIntent.CreditNoteInvoiceItem(
                                invoiceItem,
                                invoiceItem.quantity,
                                true
                            )
                        )
                    } else {
                        viewModel.handleIntent(
                            InvoiceIntent.ItemForPartialQuantityUpdate(
                                invoiceItem
                            )
                        )
                        isQuantitySheetOpen = true
                    }
                }
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
        AlertDialogWithTextInput(
            onDismissRequest = { openCreditNoteConfirmationDialog = false },
            onConfirmation = { returnDescription ->
                openCreditNoteConfirmationDialog = false
                viewModel.handleIntent(InvoiceIntent.ConfirmCreditNote(returnDescription))
            },
            dialogTitle = "Return Product",
            dialogText = "Are you sure you want to return product(s)?",
            icon = Icons.Default.Info,
            placeholderText = "Enter Reason"
        )
    }

    if (isQuantitySheetOpen) {
        TextFieldBottomSheet(
            label = "Enter Return Quantity",
            onItemSelected = {
                if (it.isNotEmpty() && it.isDigitsOnly()) {
                    state.invoiceItemForPartialQuantityUpdate?.let { item ->
                        if (it.toInt() >0 && it.toInt() <= item.quantity) {
                            viewModel.handleIntent(
                                InvoiceIntent.CreditNoteInvoiceItem(
                                    item,
                                    it.toInt(),
                                    true
                                )
                            )
                            isQuantitySheetOpen = false
                        } else {
                            Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = {
                isQuantitySheetOpen = false
            }
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