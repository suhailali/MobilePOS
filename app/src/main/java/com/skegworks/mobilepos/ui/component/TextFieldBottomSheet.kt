package com.skegworks.mobilepos.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.utils.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldBottomSheet(
    label: String,
    onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Implementation of a simple bottom sheet that displays a text field
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val state = remember { mutableStateOf("") }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SimpleTextField(
                textState = state.value,
                label = label
            ) {
                state.value = it
            }
            Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
            Button(onClick = {
                onItemSelected(
                    state.value
                )
            }) {
                Text("Done")
            }
            Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        }
    }
}