package com.skegworks.mobilepos.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LongPressAlertDialog(
    title: String,
    description: String,
    holdTime: Long = 5000L,
    onDismiss: () -> Unit,
    onFinish: () -> Unit
) {
    // State to track if the button is currently being held
    // State to track the progress for the progress indicator
    var holdProgress by remember { mutableStateOf(0f) }
    val requiredHoldTime = holdTime // 5 seconds default

    // Animate the progress bar smoothly
    val animatedProgress by animateFloatAsState(targetValue = holdProgress, label = "hold_progress")

    val interactionSource = remember { MutableInteractionSource() }
    val isHolding by interactionSource.collectIsPressedAsState()

    // LaunchedEffect to manage the timer logic when holding state changes
    LaunchedEffect(isHolding) {
        if (isHolding) {
            // Run a loop for the duration of the required hold time
            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < requiredHoldTime) {
                // Delay briefly and update the progress
                delay(16L) // Update frequently for smooth progress
                val elapsedTime = System.currentTimeMillis() - startTime
                holdProgress = elapsedTime / requiredHoldTime.toFloat()
            }
            // If the loop finishes without being canceled (released), finish the activity
            onFinish()
        } else {
            // Reset progress immediately when released
            holdProgress = 0f
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(description)
                Spacer(modifier = Modifier.height(16.dp))
                // Progress Indicator
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            // Button to hold
            Button(
                onClick = {},
                interactionSource = interactionSource
            ) {
                Text(if (isHolding) "Holding..." else "Hold")
            }
        },
        dismissButton = {
            // Standard dismiss button
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Helper function to detect press and release events using pointerInput
suspend fun PointerInputScope.detectPressGestures(
    onPress: () -> Unit,
    onRelease: () -> Unit,
) {
    awaitEachGesture {
        awaitFirstDown()
        onPress()
        // Wait for the finger to be lifted or for a cancellation event
        if (waitForUpOrCancellation() == null) {
            // A cancellation occurred (e.g., system event), handle as a release
            onRelease()
        } else {
            // Successfully released
            onRelease()
        }
    }
}
