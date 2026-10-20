package com.skegworks.mobilepos.customer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium

@Composable
fun AddCustomerScreen(
    modifier: Modifier,
    viewModel: CustomerViewModel,
    isUpdate: Boolean = false,
    onSuccess: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (!isUpdate) {
            viewModel.handleIntent(AddCustomerIntent.SetData)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpacerLarge()
        Text(if (isUpdate) "Update Customer" else "Add New Customer", style = MaterialTheme.typography.titleLarge)
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
            if (isUpdate) {
                viewModel.handleCustomerDetailsIntent(CustomerDetailsIntent.Update)
            } else {
                viewModel.handleIntent(AddCustomerIntent.Save)
            }

        }) {
            Text(if (isUpdate) "Update" else "Save")
        }
        SpacerMedium()
        if (state.value.isSaved) {
            Text("Customer Saved Successfully")
            onSuccess()
        }
        SpacerLarge()
        if (isUpdate) {
            Button(onClick = {
                val builder = StringBuilder()
                builder.append("Name: ${state.value.textStateName}").append(",")
                builder.append("Address: ${state.value.textStateAddress}").append(",")
                builder.append("Phone: ${state.value.textStatePhone}").append(",")
                builder.append("Email: ${state.value.textStateEmail}")
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Customer", builder.toString()))
            }) {
                Text("Copy")
            }
        } else {
            Button(onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val text = clipboard.primaryClip?.getItemAt(0)?.text
                val parts = text?.split(",")
                val name = parts?.get(0)?.split(":")?.get(1)?.trim()
                val address = parts?.get(1)?.split(":")?.get(1)?.trim()
                val phone = parts?.get(2)?.split(":")?.get(1)?.trim()
                val email = parts?.get(3)?.split(":")?.get(1)?.trim()
                viewModel.handleIntent(AddCustomerIntent.UpdateName(name ?: ""))
                viewModel.handleIntent(AddCustomerIntent.UpdateAddress(address ?: ""))
                viewModel.handleIntent(AddCustomerIntent.UpdatePhone(phone ?: ""))
                viewModel.handleIntent(AddCustomerIntent.UpdateEmail(email ?: ""))
            }) {
                Text("Paste")
            }
        }

        state.value.error?.let {
            Text(it, color = Color.Red)
        }
    }
}