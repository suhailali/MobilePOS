package com.skegworks.mobilepos.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AlertDialogWithTextInput(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    placeholderText: String,
) {
    var textInput by remember { mutableStateOf("") }
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text(placeholderText) },
                singleLine = true
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(textInput)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}