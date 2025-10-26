package com.skegworks.mobilepos.splash

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.R

@Composable
fun SplashLogo() {
    var alpha by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 8000)
        ) { value, _ -> alpha = value }
    }

    Image(
        painter = painterResource(id = android.R.drawable.star_on),
        contentDescription = null,
        modifier = Modifier
            .alpha(alpha)
            .size(120.dp)
    )
}