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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.pos.POSIntent
import com.skegworks.mobilepos.ui.component.SimpleTextField
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
        viewModel.handleIntent(AddProductIntent.GetUserRole)
        viewModel.fetchProducts()
    }
    // Implementation for Product List Screen goes here
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        // UI components to display the list of products
        SpacerLarge()
        SpacerMedium()
        ProductHeading(state.value.isUserStaff.not()) {
            viewModel.handleIntent(AddProductIntent.NavigateToAddProduct)
            onNavigateToAddProduct()
        }
        SpacerMedium()
        ProductFilter(state.value.textStateProductSearchTerm, { text ->
            viewModel.handleIntent(AddProductIntent.UpdateSearchTerm(text))
        }) {
            viewModel.handleIntent(AddProductIntent.SearchItem)
        }
        ProductList(state.value.products) { id ->
            if (state.value.isUserStaff.not()) {
                onNavigateToProductDetail(id)
            }
        }
    }
}

@Composable
fun ProductFilter(productSearchTerm: String, onTextChange: (String) -> Unit, onSearch: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SimpleTextField(productSearchTerm, "Enter SKU") {
            onTextChange(it)
        }
        Button(onClick = {
            onSearch()
        }) {
            Text("Search")
        }
    }
}

@Composable
fun ProductHeading(enableAddProduct: Boolean, navigate: () -> Unit) {
    // Implementation for Product Heading UI goes here
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("Products", style = MaterialTheme.typography.titleLarge, fontWeight = Bold)
        Button(
            enabled = enableAddProduct,
            onClick = { navigate() }) {
            Text("Add Product")
        }
        TextButton(onClick = {  }) {
            Text("Sync Product")
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

