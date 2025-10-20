package com.skegworks.mobilepos.vendors

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.skegworks.mobilepos.data.Vendor


@Composable
fun VendorListScreen(modifier: Modifier, vendorList: List<Vendor>) {
    Column {
        VendorFilter()
        VendorHeading()
        VendorList(vendorList)
    }
}

@Composable
fun VendorListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Name", modifier = Modifier.weight(1.5f))
        Text("State", modifier = Modifier.weight(1f))
        Text("GST%", modifier = Modifier.weight(1f))
        Text("Email", modifier = Modifier.weight(1f))
        Text("Phone", modifier = Modifier.weight(1f))
    }
}

@Composable
fun VendorListRow(vendor: Vendor) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(vendor.name, modifier = Modifier.weight(1.5f))
        Text(vendor.state, modifier = Modifier.weight(1f))
        Text(vendor.gstPercentage, modifier = Modifier.weight(1f))
        Text(vendor.email, modifier = Modifier.weight(1f))
        Text(vendor.phone, modifier = Modifier.weight(1f))
    }
}

@Composable
fun VendorList(vendorList: List<Vendor>) {
    VendorListHeader()
    LazyColumn {
       items(vendorList) { vendor ->
           VendorListRow(vendor)
       }
    }
}

@Composable
fun VendorHeading() {
    Row {
        Text("Vendors")
        Button(onClick = {}) {
            Text("Add Vendor")
        }
    }
}

@Composable
fun VendorFilter() {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        VendorNameFilter()
        VendorNumberFilter()
        VendorStateFilter()
    }
}

@Composable
fun VendorNameFilter() {
    var textState by remember { mutableStateOf(TextFieldValue("")) } // For simple text
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            textState = newTextState
        },
        label = { Text("Vendor name") },
    )
}

@Composable
fun VendorNumberFilter() {
    var textState by remember { mutableStateOf(TextFieldValue("")) } // For simple text
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            textState = newTextState
        },
        label = { Text("Vendor phone") },
    )
}

@Composable
fun VendorStateFilter() {
    var textState by remember { mutableStateOf(TextFieldValue("")) } // For simple text
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            textState = newTextState
        },
        label = { Text("Vendor state") },
    )
}