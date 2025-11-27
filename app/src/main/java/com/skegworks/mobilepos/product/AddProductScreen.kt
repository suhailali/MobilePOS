package com.skegworks.mobilepos.product

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.ui.component.ReadOnlyTextField
import com.skegworks.mobilepos.ui.component.SimpleBottomSheet
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun AddProductScreen(modifier: Modifier, viewModel: ProductViewModel) {
    val state = viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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

    LaunchedEffect(Unit) {
        viewModel.handleIntent(AddProductIntent.LoadCategories)
        viewModel.handleIntent(AddProductIntent.LoadVendors)
    }

    LaunchedEffect(state.value.textStateSku) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("SKU", state.value.textStateSku))
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpacerMedium()
        ReadOnlyTextField(textState = state.value.textStateCategoryName, "Category") {
            isCategorySheetOpen = true
        }
        ReadOnlyTextField(textState = state.value.textStateVendorName, "Vendor") {
            isVendorSheetOpen = true
        }
        SimpleTextField(textState = state.value.textStateTitle, "Title") {
            viewModel.handleIntent(AddProductIntent.UpdateTitle(it))
        }

        SpacerMedium()

        SimpleTextField(textState = state.value.textStateItemPrice.toString(), "Item Price") {
            val price = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(AddProductIntent.UpdateItemPrice(price))
        }

        ReadOnlyTextField(
            textState = state.value.textStateInputGstPercentage.toString(),
            "Input GST %"
        ) {
            isInputGstPercentageSheetOpen = true
        }
        ReadOnlyTextField(
            textState = state.value.textStateOutputGstPercentage.toString(),
            "Output GST %"
        ) {
            isOutputGstPercentageSheetOpen = true
        }

        ReadOnlyTextField(textState = state.value.textStateSaleMargin.toString(), "Sale Margin") {
            isSaleMarginSheetOpen = true
        }

        ReadOnlyTextField(
            textState = state.value.textStateDiscountPercentage.toString(),
            "Discount Percentage"
        ) {
            isDiscountSheetOpen = true
        }

        if (state.value.errorCalculatePrice) {
            Text(
                text = state.value.validationErrorMessageCalculatePrice,
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

        ReadOnlyTextField(textState = state.value.textStateInputGst.toString(), "Input GST") {

        }

        ReadOnlyTextField(textState = state.value.textStateCost.toString(), "Cost") {
            // No action on click
        }

        ReadOnlyTextField(
            textState = state.value.textStateSalePriceWithoutGst.toString(),
            "Price With Margin Without GST"
        ) {

        }

        ReadOnlyTextField(
            textState = state.value.textStateDiscountAmount.toString(),
            "Discount Amount"
        ) {

        }

        ReadOnlyTextField(
            textState = state.value.textStatePriceAfterDiscountWithoutGst.toString(),
            "Price After Discount Without GST"
        ) {

        }

        ReadOnlyTextField(textState = state.value.textStateOutputGst.toString(), "Output GST") {

        }
        ReadOnlyTextField(
            textState = state.value.textStateSalePrice.toString(),
            "Sale Price With GST"
        ) {

        }
        ReadOnlyTextField(
            textState = state.value.textStateSalePriceWithoutDiscount.toString(),
            "Sale Price Without Discount"
        ) {

        }
        ReadOnlyTextField(
            textState = state.value.textStateFinalRoundedOffPrice.toString(),
            "Final Round Off Price"
        ) {

        }

        SpacerMedium()
        ReadOnlyTextField(textState = state.value.textStateQuantity.toString(), "Quantity") {
            isQuantitySheetOpen = true
        }
        ReadOnlyTextField(
            textState = state.value.textStateAlertQuantity.toString(),
            "Alert Quantity",
        ) {
            isAlertQuantitySheetOpen = true
        }
        SimpleTextField(textState = state.value.textStateHsnCode, "HSN Code") {
            viewModel.handleIntent(AddProductIntent.UpdateHsnCode(it))
        }

        SimpleTextField(textState = state.value.textStateDescription, "Description") {
            viewModel.handleIntent(AddProductIntent.UpdateDescription(it))
        }
//        SimpleTextField(textState = state.value.textStateImageUrl, "Image URL") {
//            viewModel.handleIntent(AddProductIntent.UpdateImageUrl(it))
//        }
        if (state.value.errorSave) {
            Text(
                text = state.value.validationErrorMessageSave,
                modifier = Modifier.padding(Dimens.SMALL_PADDING.dp),
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        ReadOnlyTextField(textState = state.value.textStateSku, "SKU") {
            // No action on click
        }

        if (state.value.errorSku) {
            Text(
                text = state.value.validationErrorMessageSku,
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
        state.value.barcodeBitmap?.let { bmp ->
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Barcode",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        ReadOnlyTextField(textState = state.value.textStateSize, "Size") {
            isSizeSheetOpen = true
        }
        ReadOnlyTextField(textState = state.value.textStateColor, "Color") {
            isColorSheetOpen = true
        }
        Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        )
        {
            Button(onClick = {
                viewModel.handleIntent(AddProductIntent.Save)
            }) {
                Text("Save")
            }

            Button(onClick = {
                viewModel.handleIntent(AddProductIntent.AddAnother)
            }) {
                Text("Add Another Size/Color")
            }
        }

        if (state.value.isSaved) {
            Text("Value Saved Successfully")
        }
    }
    if (isCategorySheetOpen) {
        SimpleBottomSheet(
            list = state.value.categories,
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
            list = state.value.vendors,
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
            list = state.value.gstPercentages,
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
            list = state.value.gstPercentages,
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
            list = state.value.saleMargins,
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
            list = state.value.color,
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
            list = state.value.size,
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
            list = state.value.discountPercentages,
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
            list = state.value.quantity,
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
            list = state.value.quantity,
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
}