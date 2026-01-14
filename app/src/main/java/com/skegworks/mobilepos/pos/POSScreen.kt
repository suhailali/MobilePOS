package com.skegworks.mobilepos.pos

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.home.HomeActivity
import com.skegworks.mobilepos.product.ProductDetailIntent
import com.skegworks.mobilepos.ui.component.DeleteButton
import com.skegworks.mobilepos.ui.component.SimpleAlertDialog
import com.skegworks.mobilepos.ui.component.SpacerSmall
import com.skegworks.mobilepos.ui.component.TextFieldBottomSheet
import com.skegworks.mobilepos.utils.Dimens
import com.skegworks.mobilepos.utils.findActivity

@Composable
fun POSScreen(modifier: Modifier, viewModel: POSViewModel, navigator: POSNavigator) {
    val state by viewModel.state.collectAsState()
    var isDiscountSheetOpen by remember { mutableStateOf(false) }
    var isErrorCouponDialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                POSEvents.ERROR_INVOICE_NOT_GENERATED -> {
                    Toast.makeText(context, "Print Invoice", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getInvoiceNumber()
        viewModel.getBusiness()
    }
    LaunchedEffect(state.pdfGenerated) {
        if (state.pdfGenerated) {
            state.invoicePDF?.let {
                viewModel.printPdf(context, it, state.invoiceNumber)
            }
        }
    }

    LaunchedEffect(state.paymentComplete) {
        if (state.paymentComplete) {
            val intent = Intent(context, HomeActivity::class.java)
            Toast.makeText(context, "Payment Complete", Toast.LENGTH_SHORT).show()
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
            context.findActivity()?.finish()
        }
    }

    LaunchedEffect(state.errorCoupon) {
        if (state.errorCoupon != null) {
            isErrorCouponDialogOpen = true
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SpacerSmall()
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
            }
            SpacerSmall()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    enabled = state.invoiceItems.isNotEmpty(),
                    onClick = { viewModel.handleIntent(POSIntent.Payment) }) {
                    Text("Pay")
                }

                Button(
                    enabled = state.invoiceItems.isNotEmpty(),
                    onClick = {
                        viewModel.handleIntent(POSIntent.SendInvoice)
                    }) {
                    Text("Send")
                }
            }
            SpacerSmall()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Invoice No. ${state.invoiceNumber}")
                Text("Date: ${state.invoiceDate}")
            }
            SpacerSmall()
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
            SpacerSmall()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Amount ${state.totalPrice}")
                Text("Discount ${state.totalDiscount}")
            }
            SpacerSmall()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "To Pay ${state.finalPriceToPay}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(Dimens.MEDIUM_PADDING.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Coupon: ${state.coupon?.title}")
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Amount: ${state.couponDiscount}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("   |   Percentage: ${state.coupon?.discountPercentage}%")
                    }
                    Text("Code: ${state.coupon?.discountCode}")
                }
                DeleteButton {
                    viewModel.handleIntent(POSIntent.RemoveCoupon)
                }
            }
        }

        SpacerSmall()

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

        if (isErrorCouponDialogOpen) {
            SimpleAlertDialog(
                onDismissRequest = {
                    isErrorCouponDialogOpen = false
                    viewModel.handleIntent(POSIntent.ClearCouponError)
                },
                onConfirmation = {
                    isErrorCouponDialogOpen = false
                    viewModel.handleIntent(POSIntent.ClearCouponError)
                },
                dialogTitle = "Coupon Invalid",
                dialogText = state.errorCoupon.toString(),
                icon = Icons.Default.Info,
                positiveButtonText = "",
                negativeButtonText = "Okay"
            )
        }
    }
}

@Composable
fun POSListRow(index: Int, invoiceItem: InvoiceItem, onDelete: () -> Unit) {
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryFixed else MaterialTheme.colorScheme.primaryFixedDim
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