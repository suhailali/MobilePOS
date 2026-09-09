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
fun CategoryFilterChips(
    selectedFilter: CategoryFilter,
    onFilterSelected: (CategoryFilter) -> Unit
) {
    val filters = listOf(
        CategoryFilter.PRODUCT_CATEGORY to "Category",
        CategoryFilter.VENDOR to "Vendor",
        CategoryFilter.TIME to "Time",
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