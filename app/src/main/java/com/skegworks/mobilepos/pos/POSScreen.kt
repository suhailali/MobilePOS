package com.skegworks.mobilepos.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.ui.component.DeleteButton
import com.skegworks.mobilepos.utils.Dimens

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("POS")
                Button(onClick = {
                    viewModel.handleIntent(POSIntent.AddProduct)
                    //navigator.navigateToAddItem()
                }) {
                    Text("BarCode")
                }

                Button(onClick = {
                    viewModel.handleIntent(POSIntent.PrintInvoice)
                }) {
                    Text("Print")
                }

                Button(onClick = { viewModel.handleIntent(POSIntent.Payment) }) {
                    Text("Pay")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Invoice No. : 12362782")
                Text("Date : 12-12-2023")
            }

            if (state.value.customer != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    state.value.customer?.let {
                        Text("Customer Name: ${it.name}")
                        Text("Customer Phone: ${it.phone}")
                    }
                }
            } else {
                Button(onClick = {
                    navigator.navigateToCustomerScreen()
                }) {
                    Text("Add Customer")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total Amount ${state.value.totalPrice}")
            Text("Discount ${state.value.totalDiscount}")
        }
        Text("To Pay ${state.value.finalPriceToPay}")


        Column {
            LazyColumn {
                itemsIndexed(state.value.invoiceItems) { index, invoiceItem ->
                    POSListRow(index, invoiceItem) {
                        viewModel.handleIntent(POSIntent.RemoveItem(invoiceItem))
                    }
                }
            }

        }
    }
}

@Composable
fun POSListRow(index: Int, invoiceItem: InvoiceItem, onDelete: ()-> Unit) {
    val backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(backgroundColor)
            .padding(Dimens.MEDIUM_PADDING.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(invoiceItem.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            DeleteButton {
               onDelete()
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
            Text("Disc. Amount: ${invoiceItem.discountAmount}", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Final Price: ${invoiceItem.finalRoundedOffPrice}", modifier = Modifier.weight(1f))
        }
        Text("Net Amount: ${invoiceItem.finalRoundedOffPrice * invoiceItem.quantity}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}