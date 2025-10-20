package com.skegworks.mobilepos.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.skegworks.mobilepos.ui.component.SimpleTextField

@Composable
fun AddProductScreen(modifier: Modifier, viewModel: ProductViewModel) {
    val state = viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleTextField(textState = state.value.textStateCategoryName, "Category") {
            viewModel.handleIntent(AddProductIntent.UpdateCategory(it))
        }
        SimpleTextField(textState = state.value.textStateVendorName, "Vendor") {
            viewModel.handleIntent(AddProductIntent.UpdateVendor(it))
        }
        SimpleTextField(textState = state.value.textStateSku, "SKU") {
            viewModel.handleIntent(AddProductIntent.UpdateSku(it))
        }
        SimpleTextField(textState = state.value.textStateTitle, "Title") {
            viewModel.handleIntent(AddProductIntent.UpdateTitle(it))
        }
        SimpleTextField(textState = state.value.textStateSize, "Size") {
            viewModel.handleIntent(AddProductIntent.UpdateSize(it))
        }
        SimpleTextField(textState = state.value.textStateColor, "Color") {
            viewModel.handleIntent(AddProductIntent.UpdateColor(it))
        }
        SimpleTextField(textState = state.value.textStateCost.toString(), "Cost") {
            val cost = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(AddProductIntent.UpdateCost(cost))
        }
        SimpleTextField(textState = state.value.textStateSalePrice.toString(), "Sale Price", ) {
            val salePrice = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(AddProductIntent.UpdateSalePrice(salePrice))
        }
        SimpleTextField(textState = state.value.textStateQuantity.toString(), "Quantity", ) {
            val quantity = it.toIntOrNull() ?: 0
            viewModel.handleIntent(AddProductIntent.UpdateQuantity(quantity))
        }
        SimpleTextField(textState = state.value.textStateAlertQuantity.toString(), "Alert Quantity", ) {
            val alertQuantity = it.toIntOrNull() ?: 0
            viewModel.handleIntent(AddProductIntent.UpdateAlertQuantity(alertQuantity))
        }
        SimpleTextField(textState = state.value.textStateHsnCode, "HSN Code") {
            viewModel.handleIntent(AddProductIntent.UpdateHsnCode(it))
        }
        SimpleTextField(textState = state.value.textStateGstPercentage.toString(), "GST Percentage") {
            val gstPercentage = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(AddProductIntent.UpdateGstPercentage(gstPercentage))
        }
        SimpleTextField(textState = state.value.textStateDiscountPercentage.toString(), "Discount Percentage") {
            val discountPercentage = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(AddProductIntent.UpdateDiscountPercentage(discountPercentage))
        }
        SimpleTextField(textState = state.value.textStateDescription, "Description") {
            viewModel.handleIntent(AddProductIntent.UpdateDescription(it))
        }
        SimpleTextField(textState = state.value.textStateImageUrl, "Image URL") {
            viewModel.handleIntent(AddProductIntent.UpdateImageUrl(it))
        }

        Button(onClick = {
            viewModel.handleIntent(AddProductIntent.Save)
        }) {
            Text("Save")
        }

        if (state.value.isSaved) {
            Text("Value Saved Successfully")
        }
    }
}