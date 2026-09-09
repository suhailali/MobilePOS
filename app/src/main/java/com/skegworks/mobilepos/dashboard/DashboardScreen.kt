package com.skegworks.mobilepos.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import network.chaintech.cmpcharts.axis.DataCategorySettings
import network.chaintech.cmpcharts.ui.barchart.BarChart
import network.chaintech.cmpcharts.ui.barchart.config.BarChartConfig
import network.chaintech.cmpcharts.ui.barchart.config.BarChartStyle
import network.chaintech.cmpcharts.ui.barchart.config.BarChartType
import network.chaintech.cmpcharts.ui.barchart.config.SelectionHighlightData

@Composable
fun DashboardScreen(modifier: Modifier, viewModel: DashboardViewModel,
                    navigateToMonthlySales: () -> Unit,
                    navigateToInventorySales: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }
    Column(modifier = modifier) {
        SpacerLarge()
        Button(
            onClick = { navigateToInventorySales() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Inventory Sales")
        }

        Button(
            onClick = { navigateToMonthlySales() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Monthly Sales")
        }

        SpacerLarge()

        DateFilterChips(
            selectedFilter = state.dateFilterSelected,
            onFilterSelected = viewModel::selectDateFilter
        )
        SpacerLarge()
        CategoryFilterChips(
            selectedFilter = state.categoryFilterSelected,
            onFilterSelected = viewModel::selectCategoryFilter
        )
        when (state.isLoading) {
            true -> {
                Text(text = "Loading")
            }

            false -> {

                Column(modifier = Modifier.padding(10.dp)) {
                    SpacerLarge()
                    Text("Total sales this ${state.dateFilterSelected.name} : ${state.totalSales}")
                    SpacerMedium()
                    Text("Average sales this ${state.dateFilterSelected.name} : ${state.averageSales}")
                    SpacerLarge()
                }
                val barData = state.salesPerDay ?: listOf()

                if (barData.isNotEmpty()) {

                    val xAxisData = AxisProperties(
                        stepSize = 30.dp,
                        stepCount = 1,
                        bottomPadding = 0.dp,
                        offset = 10.dp,
                        labelRotationAngle = 90f,
                        labelPadding = 0.dp,
                        labelFormatter = { index -> barData[index].label + " on $index" },
                    )

                    val yAxisData = AxisProperties(
                        stepCount = state.yStepSize,
                        labelPadding = 20.dp,
                        offset = 0.dp,
                        categoryOptions = DataCategorySettings(
                        ),
                        labelFormatter = { index -> (index * (state.maxRange / state.yStepSize)).toString() }
                    )

                    val barChartData = BarChartConfig(
                        chartData = barData,
                        xAxisData = xAxisData,
                        yAxisData = yAxisData,
                        barStyle = BarChartStyle(
                            cornerRadius = 5.dp,
                            paddingBetweenBars = 20.dp,
                            barWidth = 30.dp,
                            selectionHighlightData = SelectionHighlightData(
                                highlightBarColor = Color.Gray,
                                highlightTextColor = Color.White,
                                highlightTextTypeface = FontWeight.Bold,
                                highlightTextBackgroundColor = Color.Magenta,
                                popUpLabel = { x, y -> " Value : $y on $x" }
                            )
                        ),
                        barChartType = BarChartType.VERTICAL
                    )
                    BarChart(modifier = Modifier.height(450.dp), barChartData = barChartData)
                }
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