package com.skegworks.mobilepos.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun SpacerLarge() {
    Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
}

@Composable
fun SpacerMedium() {
    Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
}

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
}