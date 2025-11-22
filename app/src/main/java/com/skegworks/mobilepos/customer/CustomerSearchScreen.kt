package com.skegworks.mobilepos.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun CustomerSearchScreen(modifier: Modifier, viewModel: CustomerViewModel, onSelect: (Customer) -> Unit, navigateToAddCustomer: () -> Unit) {
    val state = viewModel.stateSearch.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpacerLarge()
        Button(onClick = {
            navigateToAddCustomer()
        }) {
            Text("Add Customer")
        }
        SpacerLarge()
        SimpleTextField(textState = state.value.textStatePhone, "Search Phone Number") {
            viewModel.handleSearchIntent(SearchCustomerIntent.SearchPhone(it))
        }
        SpacerMedium()
        LazyColumn {
            itemsIndexed(state.value.customers) { index, item ->
                CustomerListRow(index, item) { selected ->
                    onSelect(selected)
                }
            }
        }
    }
}

@Composable
fun CustomerListRow(index: Int, customer: Customer, onSelect: (customer: Customer) -> Unit) {
    val backgroundColor = if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.inversePrimary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.EXTRA_SMALL_PADDING.dp)
            .background(backgroundColor)
            .clickable{
                onSelect(customer)
            },
        horizontalArrangement = Arrangement.Absolute.SpaceAround,
    ) {
        Text(customer.name, modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        Text(customer.phone, modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
    }
}