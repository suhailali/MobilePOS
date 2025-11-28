package com.skegworks.mobilepos.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.ui.component.ReadOnlyTextField
import com.skegworks.mobilepos.ui.component.SimpleAlertDialog
import com.skegworks.mobilepos.ui.component.SimpleBottomSheet
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun ProductDetailScreen(
    modifier: Modifier,
    viewModel: ProductViewModel,
    productId: String,
    navigateBack: () -> Unit
) {

    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()


    LaunchedEffect(key1 = Unit) {
        viewModel.navigateBack.collect { shouldGoBack ->
            if (shouldGoBack) {
                navigateBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleProductDetailIntent(ProductDetailIntent.FetchProduct(productId))
    }

    var isCategorySheetOpen by remember { mutableStateOf(false) }
    var isVendorSheetOpen by remember { mutableStateOf(false) }
    var isInputGstPercentageSheetOpen by remember { mutableStateOf(false) }
    var isOutputGstPercentageSheetOpen by remember { mutableStateOf(false) }
    var isSaleMarginSheetOpen by remember { mutableStateOf(false) }
    var isSizeSheetOpen by remember { mutableStateOf(false) }
    var isColorSheetOpen by remember { mutableStateOf(false) }
    var isDiscountSheetOpen by remember { mutableStateOf(false) }
    var isQuantitySheetOpen by remember { mutableStateOf(false) }
    var isAlertQuantitySheetOpen by remember { mutableStateOf(false) }
    var openDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(AddProductIntent.LoadCategories)
        viewModel.handleIntent(AddProductIntent.LoadVendors)
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpacerMedium()
        ReadOnlyTextField(textState = state.textStateCategoryName, "Category") {
            isCategorySheetOpen = true
        }
        ReadOnlyTextField(textState = state.textStateVendorName, "Vendor") {
            isVendorSheetOpen = true
        }
        SimpleTextField(textState = state.textStateTitle, "Title") {
            viewModel.handleIntent(AddProductIntent.UpdateTitle(it))
        }
        SpacerMedium()

        SimpleTextField(textState = state.textStateItemPrice.toString(), "Item Price") {
            val price = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(AddProductIntent.UpdateItemPrice(price))
        }

        ReadOnlyTextField(
            textState = state.textStateInputGstPercentage.toString(),
            "Input GST %"
        ) {
            isInputGstPercentageSheetOpen = true
        }
        ReadOnlyTextField(
            textState = state.textStateOutputGstPercentage.toString(),
            "Output GST %"
        ) {
            isOutputGstPercentageSheetOpen = true
        }

        ReadOnlyTextField(textState = state.textStateSaleMargin.toString(), "Sale Margin") {
            isSaleMarginSheetOpen = true
        }

        ReadOnlyTextField(
            textState = state.textStateDiscountPercentage.toString(),
            "Discount Percentage"
        ) {
            isDiscountSheetOpen = true
        }

        if (state.errorCalculatePrice) {
            Text(
                text = state.validationErrorMessageCalculatePrice,
                modifier = Modifier.padding(Dimens.SMALL_PADDING.dp),
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
        Button(onClick = {
            viewModel.handleIntent(AddProductIntent.CalculatePricing)
        }) {
            Text("Calculate Cost & Price with GST")
        }
        Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))

        ReadOnlyTextField(textState = state.textStateInputGst.toString(), "Input GST") {

        }

        ReadOnlyTextField(textState = state.textStateCost.toString(), "Cost") {
            // No action on click
        }

        ReadOnlyTextField(
            textState = state.textStateSalePriceWithoutGst.toString(),
            "Price With Margin Without GST"
        ) {

        }

        ReadOnlyTextField(
            textState = state.textStateDiscountAmount.toString(),
            "Discount Amount"
        ) {

        }

        ReadOnlyTextField(
            textState = state.textStatePriceAfterDiscountWithoutGst.toString(),
            "Price After Discount Without GST"
        ) {

        }

        ReadOnlyTextField(textState = state.textStateOutputGst.toString(), "Output GST") {

        }
        ReadOnlyTextField(
            textState = state.textStateSalePrice.toString(),
            "Sale Price With GST"
        ) {

        }
        ReadOnlyTextField(
            textState = state.textStateSalePriceWithoutDiscount.toString(),
            "Sale Price Without Discount"
        ) {

        }
        ReadOnlyTextField(
            textState = state.textStateFinalRoundedOffPrice.toString(),
            "Final Round Off Price"
        ) {

        }

        SpacerMedium()
        ReadOnlyTextField(textState = state.textStateQuantity.toString(), "Quantity") {
            isQuantitySheetOpen = true
        }
        ReadOnlyTextField(
            textState = state.textStateAlertQuantity.toString(),
            "Alert Quantity",
        ) {
            isAlertQuantitySheetOpen = true
        }
        SimpleTextField(textState = state.textStateHsnCode, "HSN Code") {
            viewModel.handleIntent(AddProductIntent.UpdateHsnCode(it))
        }

        SimpleTextField(textState = state.textStateDescription, "Description") {
            viewModel.handleIntent(AddProductIntent.UpdateDescription(it))
        }
//        SimpleTextField(textState = state.textStateImageUrl, "Image URL") {
//            viewModel.handleIntent(AddProductIntent.UpdateImageUrl(it))
//        }
        if (state.errorSave) {
            Text(
                text = state.validationErrorMessageSave,
                modifier = Modifier.padding(Dimens.SMALL_PADDING.dp),
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        SimpleTextField(textState = state.textStateSku, "SKU") {
            viewModel.handleIntent(AddProductIntent.UpdateSku(it))
        }

        if (state.errorSku) {
            Text(
                text = state.validationErrorMessageSku,
                modifier = Modifier.padding(Dimens.SMALL_PADDING.dp),
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
        Button(onClick = {
            viewModel.handleIntent(AddProductIntent.GenerateSku)
        }) {
            Text("Generate and Copy SKU")
        }
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        state.barcodeBitmap?.let { bmp ->
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Barcode",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        ReadOnlyTextField(textState = state.textStateSize, "Size") {
            isSizeSheetOpen = true
        }
        ReadOnlyTextField(textState = state.textStateColor, "Color") {
            isColorSheetOpen = true
        }
        Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        )
        {
            Button(onClick = {
                viewModel.handleProductDetailIntent(ProductDetailIntent.UpdateProduct)
            }) {
                Text("Update")
            }

            Button(onClick = {
                viewModel.handleProductDetailIntent(ProductDetailIntent.AddAnotherProduct)
            }) {
                Text("Add Another Size/Color")
            }
        }
        if (state.isSaved) {
            Text("Value Saved Successfully")
        }
        SpacerLarge()
        Button(onClick = {
            openDeleteDialog = true
        }) {
            Text("Delete")
        }

        SpacerMedium()
    }
    if (isCategorySheetOpen) {
        SimpleBottomSheet(
            list = state.categories,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateCategory(it))
                isCategorySheetOpen = false
            },
            labelSelector = {
                it.name
            }
        ) {
            isCategorySheetOpen = false
        }
    }

    if (isVendorSheetOpen) {
        SimpleBottomSheet(
            list = state.vendors,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateVendor(it))
                isVendorSheetOpen = false
            },
            labelSelector = {
                it.name
            }
        ) {
            isVendorSheetOpen = false
        }
    }
    if (isInputGstPercentageSheetOpen) {
        SimpleBottomSheet(
            list = state.gstPercentages,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateInputGstPercentage(it))
                isInputGstPercentageSheetOpen = false
            },
            labelSelector = {
                it.toString()
            }
        ) {
            isInputGstPercentageSheetOpen = false
        }
    }
    if (isOutputGstPercentageSheetOpen) {
        SimpleBottomSheet(
            list = state.gstPercentages,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateOutputGstPercentage(it))
                isOutputGstPercentageSheetOpen = false
            },
            labelSelector = {
                it.toString()
            }
        ) {
            isOutputGstPercentageSheetOpen = false
        }
    }
    if (isSaleMarginSheetOpen) {
        SimpleBottomSheet(
            list = state.saleMargins,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateSaleMargin(it))
                isSaleMarginSheetOpen = false
            },
            labelSelector = {
                it.toString()
            }
        ) {
            isSaleMarginSheetOpen = false
        }
    }
    if (isColorSheetOpen) {
        SimpleBottomSheet(
            list = state.color,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateColor(it))
                isColorSheetOpen = false
            },
            labelSelector = {
                it
            }
        ) {
            isColorSheetOpen = false
        }
    }
    if (isSizeSheetOpen) {
        SimpleBottomSheet(
            list = state.size,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateSize(it))
                isSizeSheetOpen = false
            },
            labelSelector = {
                it
            }
        ) {
            isSizeSheetOpen = false
        }
    }
    if (isDiscountSheetOpen) {
        SimpleBottomSheet(
            list = state.discountPercentages,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateDiscountPercentage(it))
                isDiscountSheetOpen = false
            },
            labelSelector = {
                it.toString()
            }
        ) {
            isDiscountSheetOpen = false
        }
    }
    if (isQuantitySheetOpen) {
        SimpleBottomSheet(
            list = state.quantity,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateQuantity(it))
                isQuantitySheetOpen = false
            },
            labelSelector = {
                it.toString()
            }
        ) {
            isQuantitySheetOpen = false
        }
    }
    if (isAlertQuantitySheetOpen) {
        SimpleBottomSheet(
            list = state.quantity,
            onItemSelected = {
                viewModel.handleIntent(AddProductIntent.UpdateAlertQuantity(it))
                isAlertQuantitySheetOpen = false
            },
            labelSelector = {
                it.toString()
            }
        ) {
            isAlertQuantitySheetOpen = false
        }
    }

    if (openDeleteDialog) {
        SimpleAlertDialog(
            onDismissRequest = { openDeleteDialog = false },
            onConfirmation = {
                openDeleteDialog = false
                state.productDetail?.let {
                    viewModel.handleProductDetailIntent(ProductDetailIntent.DeleteProduct(id = it.id))
                }
            },
            dialogTitle = "Delete Product",
            dialogText = "Are you sure you want to delete this product?",
            icon = Icons.Default.Info
        )
    }
}