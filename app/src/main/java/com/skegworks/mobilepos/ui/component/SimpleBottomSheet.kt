package com.skegworks.mobilepos.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.utils.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SimpleBottomSheet(
    list: List<T>,
    onItemSelected: (T) -> Unit,
    labelSelector: (T) -> String,
    onDismiss : () -> Unit
) {
    // Implementation of a simple bottom sheet that displays a list of items
    // and calls onItemSelected when an item is selected.
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            LazyColumn {
                items(list) { item ->
                    Text(
                        text = labelSelector(item),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelected(item)
                            }
                            .padding(Dimens.MEDIUM_PADDING.dp)
                    )
                }
            }
        }
    }
}