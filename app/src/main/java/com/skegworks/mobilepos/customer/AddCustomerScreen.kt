package com.skegworks.mobilepos.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium

@Composable
fun AddCustomerScreen(modifier: Modifier, viewModel: CustomerViewModel, onSuccess: () -> Unit) {
    val state = viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(AddCustomerIntent.SetData)
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpacerLarge()
        Text("Add New Customer", style = MaterialTheme.typography.titleLarge)
        SpacerLarge()
        SimpleTextField(textState = state.value.textStateName, "Name") {
            viewModel.handleIntent(AddCustomerIntent.UpdateName(it))
        }
        SimpleTextField(textState = state.value.textStateAddress, "Address") {
            viewModel.handleIntent(AddCustomerIntent.UpdateAddress(it))
        }
        SimpleTextField(textState = state.value.textStatePhone, "Phone") {
            viewModel.handleIntent(AddCustomerIntent.UpdatePhone(it))
        }
        SimpleTextField(textState = state.value.textStateEmail, "Email") {
            viewModel.handleIntent(AddCustomerIntent.UpdateEmail(it))
        }
        SpacerLarge()
        Button(onClick = {
            viewModel.handleIntent(AddCustomerIntent.Save)
        }) {
            Text("Save")
        }
        SpacerMedium()
        if (state.value.isSaved) {
            Text("Customer Saved Successfully")
            onSuccess()
        }

        state.value.error?.let {
            Text(it, color = Color.Red)
        }
    }
}