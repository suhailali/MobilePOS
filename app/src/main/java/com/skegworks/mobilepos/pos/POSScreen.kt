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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.ui.component.DeleteButton
import com.skegworks.mobilepos.ui.component.TextFieldBottomSheet
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun POSScreen(modifier: Modifier, viewModel: POSViewModel, navigator: POSNavigator) {
    val state by viewModel.state.collectAsState()
    var isDiscountSheetOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getInvoiceNumber()
        viewModel.getBusiness()
    }
    LaunchedEffect(state.pdfGenerated) {
        if (state.pdfGenerated) {
            state.invoicePDF?.let {
                viewModel.printPdf(context, it, "Invoice")
            }
        }
    }

    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier.height(3.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SMALL_PADDING.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    enabled = state.coupon == null,
                    onClick = {
                        navigator.navigateToAddItem(BarcodeScanType.PRODUCT)
                    }) {
                    Text("BarCode")
                }
                Button(
                    enabled = state.coupon == null && state.invoiceItems.isNotEmpty(),
                    onClick = {
                        navigator.navigateToAddItem(BarcodeScanType.COUPON)
                    }) {
                    Text("Coupon")
                }

                Button(
                    enabled = state.invoiceItems.isNotEmpty(),
                    onClick = {
                        viewModel.handleIntent(POSIntent.PrintInvoice)
                    }) {
                    Text("Print")
                }

                Button(
                    enabled = state.invoiceItems.isNotEmpty(),
                    onClick = { viewModel.handleIntent(POSIntent.Payment) }) {
                    Text("Pay")
                }
            }
            Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Invoice No. ${state.invoiceNumber}")
                Text("Date: ${state.invoiceDate}")
            }
            Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
            if (state.customer != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    state.customer?.let {
                        Text("Customer Name: ${it.name}")
                        Text("Phone: ${it.phone}")
                    }
                }
            } else {
                Button(onClick = {
                    navigator.navigateToCustomerScreen()
                }) {
                    Text("Add Customer")
                }
            }
            Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Amount ${state.totalPrice}")
                Text("Discount ${state.totalDiscount}")
            }
            Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("To Pay ${state.finalPriceToPay}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                if (state.invoiceItems.isNotEmpty()) {
                    if (state.cashDiscount > 0.0) {
                        Row {
                            Text("Cash Discount: ${state.cashDiscount}")
                            Spacer(modifier.padding(horizontal = Dimens.SMALL_PADDING.dp))
                            DeleteButton {
                                viewModel.handleIntent(POSIntent.RemoveCashDiscount)
                            }
                        }

                    } else {
                        Button(onClick = { isDiscountSheetOpen = true }) {
                            Text("Add Cash Discount")
                        }
                    }
                }
            }
        }

        if (state.coupon != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Coupon: ${state.coupon?.title}")
                Text("Discount: ${state.coupon?.discountPercentage}%")
                DeleteButton {
                    viewModel.handleIntent(POSIntent.RemoveCoupon)
                }
            }
        }

        Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))

        Column {
            LazyColumn {
                itemsIndexed(
                    items = state.invoiceItems,
                    key = { _, item -> item.id }) { index, invoiceItem ->
                    POSListRow(index, invoiceItem) {
                        viewModel.handleIntent(POSIntent.RemoveItem(invoiceItem))
                    }
                }
            }
        }
        if (isDiscountSheetOpen) {
            TextFieldBottomSheet(
                label = "Cash Discount",
                onItemSelected = {
                    viewModel.handleIntent(POSIntent.AddCashDiscount(it.toDouble()))
                    isDiscountSheetOpen = false
                },
                onDismiss = {
                    isDiscountSheetOpen = false
                }
            )
        }
    }
}

@Composable
fun POSListRow(index: Int, invoiceItem: InvoiceItem, onDelete: () -> Unit) {
    val backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            Text("Final Price: ${invoiceItem.finalRoundedOffPrice}", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Net Amount: ${invoiceItem.finalRoundedOffPrice * invoiceItem.quantity}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}