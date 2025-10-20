package com.skegworks.mobilepos.vendors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VendorDetails(modifier: Modifier, viewModel: VendorViewModel) {
    val state = viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    Column(modifier = modifier.verticalScroll(scrollState).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        //var textStateName by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(textState = state.value.textStateName, "Name") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateName(it))
        }

//        var textStateAddress by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateAddress, "Address") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateAddress(it))
        }

//        var textStateCity by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateCity, "City") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateCity(it))
        }

//        var textStateState by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateState, "State") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateState(it))
        }

//        var textStateCountry by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateCountry, "Country") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateCountry(it))
        }

//        var textStatePhone by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStatePhone, "Phone") {
            viewModel.handleIntent(VendorDetailsIntent.UpdatePhone(it))
        }

//        var textStateEmail by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateEmail, "Email") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateEmail(it))
        }

//        var textStateZipCode by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateZipCode, "ZipCode") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateZipCode(it))
        }

//        var textStateGST by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateGST, "GST") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateGST(it))
        }

//        var textStateGSTPercentage by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateGSTPercentage, "GST%") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateGSTPercentage(it))
        }

//        var textStateCurrency by remember { mutableStateOf(TextFieldValue("")) }
        VendorTextField(state.value.textStateCurrency, "Currency") {
            viewModel.handleIntent(VendorDetailsIntent.UpdateCurrency(it))
        }

        Button(onClick = {
            viewModel.handleIntent(VendorDetailsIntent.Save)
        }) {
            Text("Save")
        }

        if (state.value.isSaved) {
            Text("Value Saved Successfully")
        }
    }
}

@Composable
fun VendorTextField(textState: String, label: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            onChange(newTextState)
        },
        label = { Text(label) },
    )
}