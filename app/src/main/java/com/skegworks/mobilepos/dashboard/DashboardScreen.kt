package com.skegworks.mobilepos.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import network.chaintech.cmpcharts.axis.AxisProperties
import network.chaintech.cmpcharts.ui.barchart.BarChart
import network.chaintech.cmpcharts.ui.barchart.config.BarChartConfig
import network.chaintech.cmpcharts.ui.barchart.config.BarChartStyle
import network.chaintech.cmpcharts.ui.barchart.config.SelectionHighlightData

@Composable
fun DashboardScreen(modifier: Modifier, viewModel: DashboardViewModel) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }
    Column {

        when (state.isLoading) {
            true -> {
                Text(text = "Loading")
            }

            false -> {
                val barData = state.salesPerDay ?: listOf()

                val xAxisData = AxisProperties(
                    stepSize = 30.dp,
                    stepCount = barData.size - 1,
                    bottomPadding = 40.dp,
                    labelRotationAngle = 20f,
                    initialDrawPadding = 48.dp,
                    labelFormatter = { index -> barData[index].label }

                )

                val yAxisData = AxisProperties(
                    stepCount = state.yStepSize,
                    labelPadding = 20.dp,
                    offset = 20.dp,
                    labelFormatter = { index -> (index * (state.maxRange / state.yStepSize)).toString() }
                )

                val barChartData = BarChartConfig(
                    chartData = barData,
                    xAxisData = xAxisData,
                    yAxisData = yAxisData,
                    barStyle = BarChartStyle(
                        cornerRadius = 5.dp,
                        paddingBetweenBars = 20.dp,
                        barWidth = 25.dp,
                        selectionHighlightData = SelectionHighlightData(
                            highlightBarColor = Color.Gray,
                            highlightTextColor = Color.White,
                            highlightTextTypeface = FontWeight.Bold,
                            highlightTextBackgroundColor = Color.Magenta,
                            popUpLabel = { _, y -> " Value : $y " }
                        )
                    ),
                    horizontalExtraSpace = 10.dp,
                )

                SpacerLarge()
                Text("Total sales this week : ${state.totalSales}")
                SpacerMedium()
                Text("Average sales this week : ${state.averageSales}")
                SpacerLarge()
                BarChart(modifier = Modifier.height(350.dp), barChartData = barChartData)
            }
        }
    }
}

@Composable
fun HeaderAndLabel(modifier: Modifier, header: String, label: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = header,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Text(text = label)
    }
}