package com.skegworks.mobilepos.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ReadOnlyTextField(textState: String, label: String, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable(
        onClick = { onClick() }
    )) {
        OutlinedTextField(
            // Or TextField(...)
            value = textState,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = false
        )
    }
}