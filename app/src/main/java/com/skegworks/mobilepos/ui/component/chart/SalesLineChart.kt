package com.skegworks.mobilepos.ui.component.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.data.domain.ChartPoint
import kotlin.collections.mapIndexed

@Composable
fun SalesLineChart(data: List<ChartPoint>) {

    if (data.isEmpty()) return

    val maxValue = data.maxOf { it.yValue }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {

        val spacing = size.width / (data.size - 1)

        val points = data.mapIndexed { index, sale ->
            Offset(
                x = index * spacing,
                y = (size.height - (sale.yValue / maxValue * size.height)).toFloat()
            )
        }

        for (i in 0 until points.size - 1) {
            drawLine(
                color = Color(0xFF4CAF50),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }
    }
}