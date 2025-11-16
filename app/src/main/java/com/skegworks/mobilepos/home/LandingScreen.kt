package com.skegworks.mobilepos.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme

@Composable
fun LandingScreen(
    modifier: Modifier,
    viewModel: HomeViewModel,
    onClick: (String) -> Unit,
) {
    val state = viewModel.state.collectAsState()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(state.value.features) { item ->
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        onClick(item)
                    }) {
                Text(text = item)
            }
        }
    }
}