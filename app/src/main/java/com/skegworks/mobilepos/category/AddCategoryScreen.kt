package com.skegworks.mobilepos.category

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
import com.skegworks.mobilepos.ui.component.SimpleTextField

@Composable
fun AddCategoryScreen(modifier: Modifier, viewModel: CategoryViewModel) {
    val state = viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleTextField(textState = state.value.textStateCategoryName, "Category") {
            viewModel.handleIntent(AddCategoryIntent.UpdateCategory(it))
        }
        SimpleTextField(textState = state.value.textStateDescription, "Description") {
            viewModel.handleIntent(AddCategoryIntent.UpdateDescription(it))
        }
        Button(onClick = {
            viewModel.handleIntent(AddCategoryIntent.Save)
        }) {
            Text("Save")
        }

        if (state.value.isSaved) {
            Text("Value Saved Successfully")
        }
    }
}