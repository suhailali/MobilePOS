package com.skegworks.mobilepos.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun ProductListScreen(
    modifier: Modifier,
    viewModel: ProductViewModel,
    onNavigateToProductDetail: (id: String) -> Unit,
    onNavigateToAddProduct: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }
    // Implementation for Product List Screen goes here
    Column {
        // UI components to display the list of products
        SpacerLarge()
        ProductFilter()
        SpacerMedium()
        ProductHeading {
            viewModel.handleIntent(AddProductIntent.NavigateToAddProduct)
            onNavigateToAddProduct()
        }
        SpacerMedium()
        ProductList(state.value.products) { id ->
            onNavigateToProductDetail(id)
        }
    }
}

@Composable
fun ProductFilter() {
    // Implementation for Product Filter UI goes here
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
//        ProductNameFilter()
        ProductSkuFilter()
//        ProductCategoryFilter()
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("Products", style = MaterialTheme.typography.titleLarge, fontWeight = Bold)
        Button(onClick = { navigate() }) {
            Text("Add Product")
        }
    }
}

@Composable
fun ProductList(list: List<Product>, onClick: (id: String) -> Unit) {
    // Implementation for Product List UI goes here
    LazyColumn {
        itemsIndexed(
            items = list,
            key = { _, item -> item.id }) { index, product ->
            ProductListRow(index, product) {
                onClick(product.id)
            }
        }
    }
}

@Composable
fun ProductListRow(index: Int, product: Product, onClick: () -> Unit) {
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
            Text(product.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Rate: ${product.salePriceWithoutDiscount}", modifier = Modifier.weight(1f))
            Text("Quantity: ${product.quantity}", modifier = Modifier.weight(1f))
        }
        Text("Barcode: ${product.sku}")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Color: ${product.color}", modifier = Modifier.weight(1f))
            Text("Size: ${product.size}", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Discount %: ${product.discountPercentage}", modifier = Modifier.weight(1f))
            Text("Final Price: ${product.finalRoundedOffPrice}", modifier = Modifier.weight(1f))
        }
    }
}

