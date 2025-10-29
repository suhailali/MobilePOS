package com.skegworks.mobilepos.pos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.product.ProductListRow
import com.skegworks.mobilepos.ui.component.SimpleTextField

@Composable
fun POSScreen(modifier: Modifier, viewModel: POSViewModel, navigator: POSNavigator) {
    val state = viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    Column {
        Spacer(modifier.height(40.dp))
        Column {
            Row {
                Text("POS")
                Text("BarCode", modifier = Modifier.clickable{
                    navigator.navigateToAddItem()
                })
                Text("Pay")
            }
            Row {
                Text("Invoice No. : 12362782")
            }
            Row {
                SimpleTextField(textState = state.value.invoiceNumber, "Category") { }
            }
        }
        Column {
            LazyColumn {
                items(state.value.addedProducts) { product ->
                    ProductListRow(product)
                }
            }

        }
        Text("Total = 1250/-")

        Button(onClick = {

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
fun POSListRow(product: Product) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(product.title, modifier = Modifier.weight(1.5f))
        Text(product.itemPrice.toString(), modifier = Modifier.weight(1f))
        Text(product.sku, modifier = Modifier.weight(1f))
        Text(product.quantity.toString(), modifier = Modifier.weight(1f))
        Text(product.salePrice.toString(), modifier = Modifier.weight(1f))
    }
}