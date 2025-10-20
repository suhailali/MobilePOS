package com.skegworks.mobilepos.customer

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
import com.skegworks.mobilepos.category.AddCategoryIntent
import com.skegworks.mobilepos.category.CategoryViewModel
import com.skegworks.mobilepos.ui.component.SimpleTextField

@Composable
fun AddCustomerScreen(modifier: Modifier, viewModel: CustomerViewModel) {
    val state = viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Button(onClick = {
            viewModel.handleIntent(AddCustomerIntent.Save)
        }) {
            Text("Save")
        }

        if (state.value.isSaved) {
            Text("Value Saved Successfully")
        }
    }
}