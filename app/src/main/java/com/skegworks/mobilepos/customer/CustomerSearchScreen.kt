package com.skegworks.mobilepos.customer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.ui.component.SpacerLarge
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.utils.Dimens
import java.util.Locale.getDefault

@Composable
fun CustomerSearchScreen(
    modifier: Modifier,
    viewModel: CustomerViewModel,
    onSelect: (Customer) -> Unit,
    navigateToAddCustomer: () -> Unit
) {
    val state = viewModel.stateSearch.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.handleSearchIntent(SearchCustomerIntent.SearchPhone(""))
    }

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
        SimpleTextField(textState = state.value.textStatePhone, "Search Phone Number / Name") {
            viewModel.handleSearchIntent(SearchCustomerIntent.SearchPhone(it))
        }
        SpacerMedium()
        LazyColumn {
            itemsIndexed(state.value.customers) { index, item ->
                CustomerListRow(index, item, onSelect =  {onSelect(it)}) {
                    val phoneNumber =
                        ("91" + item.phone) // Country code + number
                    val message = "Hi " + item.name.uppercase(getDefault()) + ","

                    val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = url.toUri()
                        setPackage("com.whatsapp.w4b") // WhatsApp Business
                    }

                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fallback to normal WhatsApp
                        val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(fallbackIntent)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerListRow(index: Int, customer: Customer, onSelect: (customer: Customer) -> Unit, onSendMessage: () -> Unit) {
    val backgroundColor =
        if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.inversePrimary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.EXTRA_SMALL_PADDING.dp)
            .background(backgroundColor)
            .clickable {
                onSelect(customer)
            },
        horizontalArrangement = Arrangement.Absolute.SpaceAround,
    ) {
        Text(customer.name, modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        Text(customer.phone, modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        Button(onClick = {
            onSendMessage()
        }) {
            Text("Message")
        }
    }
}
