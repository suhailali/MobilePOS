package com.skegworks.mobilepos.ui.component.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.awaitCancellation
import java.time.format.DateTimeFormatter

@Composable
fun Clock(
    modifier: Modifier = Modifier,
    showDate: Boolean = true,
    timeFontSize: TextUnit = 48.sp,
    dateFontSize: TextUnit = 20.sp,
    dateFormat: String = "dd-MM-yyyy",
    viewModel: ClockViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val time by viewModel.time.collectAsState()
    val showColon by viewModel.showColon.collectAsState()

    // Automatically start/stop based on screen visibility
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.start()
            awaitCancellation()
        }
    }

    val dateString = time.format(DateTimeFormatter.ofPattern(dateFormat))
    val hr = "%2d".format(time.hour)
    val min = "%02d".format(time.minute)
    val sec = "%02d".format(time.second)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        // Date
        if (showDate) {
            Text(
                text = dateString,
                fontSize = dateFontSize,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(4.dp))
        }

        // Main digital time with blinking colon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = hr, fontSize = timeFontSize, fontWeight = FontWeight.Bold)

            Text(text = ":", fontSize = timeFontSize, fontWeight = FontWeight.Bold)

            Text(text = min, fontSize = timeFontSize, fontWeight = FontWeight.Bold)

            Text(text = ":", fontSize = timeFontSize, fontWeight = FontWeight.Bold)

            Text(text = sec, fontSize = timeFontSize, fontWeight = FontWeight.Bold)
        }
    }
}
