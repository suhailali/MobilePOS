package com.skegworks.mobilepos.cashcounter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.data.domain.CashCounterStatus
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun CashCounterScreen(modifier: Modifier, viewModel: CashCounterViewModel, onSuccess: () -> Unit) {

    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value.isSynced) {
        if (state.value.isSynced) {
            onSuccess()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))

        SimpleTextField(
            textState = state.value.textStateCashInCounter.toString(),
            "Cash in Counter"
        ) {
            val price = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(CashCounterIntent.UpdateCash(price))
        }

        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))

        Button(onClick = {
            if (state.value.state == CashCounterStatus.OPEN) {
                viewModel.handleIntent(CashCounterIntent.CloseCashCounter)
            } else {
                viewModel.handleIntent(CashCounterIntent.OpenCashCounter)
            }
        }) {
            if (state.value.state == CashCounterStatus.OPEN) {
                Text("Close Cash Counter")
            } else {
                Text("Open Cash Counter")
            }
        }

        Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        Text(state.value.error ?: "")
    }
}