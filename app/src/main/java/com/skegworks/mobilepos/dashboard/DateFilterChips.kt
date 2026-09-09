package com.skegworks.mobilepos.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun DateFilterChips(
    selectedFilter: DateFilter,
    onFilterSelected: (DateFilter) -> Unit
) {
    val filters = listOf(
        DateFilter.TODAY to "Today",
        DateFilter.WEEK to "Week",
        DateFilter.THIS_MONTH to "This Month",
        DateFilter.THREE_MONTHS to "3 Months",
        DateFilter.SIX_MONTHS to "6 Months",
        DateFilter.THIS_YEAR to "This Year",
        DateFilter.OVERALL to "Overall"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filters) { (filter, label) ->

            FilterChip(
                selected = selectedFilter == filter,
                onClick = {
                    onFilterSelected(filter)
                },
                label = {
                    Text(label)
                }
            )
        }
    }
}