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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.data.domain.CashCounterStatus
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun CashCounterScreen(modifier: Modifier, viewModel: CashCounterViewModel, onSuccess: () -> Unit) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchLatestCashCounter()
    }

    LaunchedEffect(state.isSynced) {
        if (state.isSynced) {
            onSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.MEDIUM_PADDING.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))

        SimpleTextField(
            textState = state.textStateCashInCounter.toString(),
            "Cash in Counter"
        ) {
            val price = it.toDoubleOrNull() ?: 0.0
            viewModel.handleIntent(CashCounterIntent.UpdateCash(price))
        }

        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))

        Button(enabled = state.buttonEnabled, onClick = {
            if (state.state == CashCounterStatus.OPEN) {
                viewModel.handleIntent(CashCounterIntent.CloseCashCounter)
            } else {
                viewModel.handleIntent(CashCounterIntent.OpenCashCounter)
            }
        }) {
            Text(state.buttonText)
        }

        Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        Text(state.error ?: "", color = Color.Red)
    }
}

enum class CashCounterAction {
    ACTION_CLOSE,
    ACTION_OPEN,
    ACTION_CLOSE_AND_OPEN
}
