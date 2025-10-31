package com.skegworks.mobilepos.pos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.product.ProductListRow

@Composable
fun POSScreen(modifier: Modifier, viewModel: POSViewModel, navigator: POSNavigator) {
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state.value.pdfGenerated) {
        if (state.value.pdfGenerated) {
            state.value.invoicePDF?.let {
                viewModel.printPdf(context, it, "Invoice")
            }
        }
    }

    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier.height(3.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("POS")
                Button(onClick = {
                    viewModel.handleIntent(POSIntent.AddProduct)
                    //navigator.navigateToAddItem()
                }) {
                    Text("BarCode")
                }

                Button(onClick = { viewModel.handleIntent(POSIntent.Payment)}) {
                    Text("Pay")
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Invoice No. : 12362782")
                Text("Date : 12-12-2023")
            }
            Row {
                Text("Customer: 12362782")
            }
//            Row {
//                SimpleTextField(textState = state.value.invoiceNumber, "Category") { }
//            }
        }
        Column {
            LazyColumn {
                items(state.value.invoiceItems) { product ->
                    POSListRow(product)
                }
            }

        }
        Text("Total Amount ${state.value.totalPrice}")
        Text("Discount ${state.value.totalDiscount}")
        Text("To Pay ${state.value.finalPriceToPay}")

        Button(onClick = {
            viewModel.handleIntent(POSIntent.PrintInvoice)
        }) {
            Text("Print")
        }
    }
}

@Composable
fun POSListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Name", modifier = Modifier.weight(1.5f))
        Text("Item Price", modifier = Modifier.weight(1f))
        Text("SKU", modifier = Modifier.weight(1f))
        Text("Quantity", modifier = Modifier.weight(1f))
        Text("Sale Price", modifier = Modifier.weight(1f))
    }
}

@Composable
fun POSListRow(invoiceItem: InvoiceItem) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(invoiceItem.title, modifier = Modifier.weight(1.5f))
        Text(invoiceItem.itemPrice.toString(), modifier = Modifier.weight(1f))
        Text(invoiceItem.sku, modifier = Modifier.weight(1f))
        Text(invoiceItem.quantity.toString(), modifier = Modifier.weight(1f))
        Text(invoiceItem.salePrice.toString(), modifier = Modifier.weight(1f))
    }
}