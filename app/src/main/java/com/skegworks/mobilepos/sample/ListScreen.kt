package com.skegworks.mobilepos.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.skegworks.mobilepos.R

@Composable
fun ListScreen(modifier: Modifier, viewModel: ListViewModel) {
    val state by viewModel.state.collectAsState()
//    val list = state.value.list
    Column(modifier=modifier.fillMaxWidth().background(Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn {
            items(state.list) { item ->
                SampleRow(item)
            }
        }
    }
}

@Composable
fun SampleRow(data: SampleData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.imageURL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape),
        )
        Text(data.description)
    }
}