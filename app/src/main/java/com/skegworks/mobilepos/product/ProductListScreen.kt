package com.skegworks.mobilepos.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.skegworks.mobilepos.data.domain.Product

@Composable
fun ProductListScreen(
    modifier: Modifier,
    viewModel: ProductViewModel,
    onNavigateToAddProduct: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }
    // Implementation for Product List Screen goes here
    Column {
        // UI components to display the list of products
        ProductFilter()
        ProductHeading() {
            onNavigateToAddProduct()
        }
        ProductList(state.value.products)
    }
}

@Composable
fun ProductFilter() {
    // Implementation for Product Filter UI goes here
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProductNameFilter()
        ProductSkuFilter()
        ProductCategoryFilter()
    }
}

@Composable
fun ProductNameFilter() {
    var textState by remember { mutableStateOf(TextFieldValue("")) } // For simple text
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            textState = newTextState
        },
        label = { Text("Product name") },
    )
}

@Composable
fun ProductSkuFilter() {
    var textState by remember { mutableStateOf(TextFieldValue("")) } // For simple text
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            textState = newTextState
        },
        label = { Text("Product SKU") },
    )
}

@Composable
fun ProductCategoryFilter() {
    var textState by remember { mutableStateOf(TextFieldValue("")) } // For simple text
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            textState = newTextState
        },
        label = { Text("Product Category") },
    )
}

@Composable
fun ProductHeading(navigate: () -> Unit) {
    // Implementation for Product Heading UI goes here
    Row {
        Text("Products")
        Button(onClick = { navigate() }) {
            Text("Add Product")
        }
    }
}

@Composable
fun ProductList(list: List<Product>) {
    // Implementation for Product List UI goes here
    ProductListHeader()
    LazyColumn {
        items(list) { product ->
            ProductListRow(product)
        }
    }
}

@Composable
fun ProductListHeader() {
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
fun ProductListRow(product: Product) {
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

