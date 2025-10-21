package com.skegworks.mobilepos.ui.component

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SimpleTextField(textState: String, label: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        // Or TextField(...)
        value = textState,
        onValueChange = { newTextState ->
            onChange(newTextState)
        },
        label = { Text(label) },
    )
}