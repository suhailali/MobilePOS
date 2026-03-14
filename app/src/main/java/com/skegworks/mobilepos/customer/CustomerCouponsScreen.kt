package com.skegworks.mobilepos.customer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.ui.component.AlertDialogWithImageView
import com.skegworks.mobilepos.ui.component.SpacerMedium
import com.skegworks.mobilepos.utils.Dimens
import com.skegworks.mobilepos.utils.toDateString
import java.util.Locale
import java.util.Locale.getDefault

@Composable
fun CustomerCouponsScreen(modifier: Modifier, viewModel: CustomerViewModel) {
    val state by viewModel.stateDetails.collectAsState()
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SpacerMedium()
        Button(onClick = {
            state.customer?.let {
                viewModel.handleCustomerDetailsIntent(CustomerDetailsIntent.CreateNewCoupon(it))
            }
        }) {
            Text("Generate New Coupon")
        }
        SpacerMedium()
        CouponList(state.couponList) {

        }
    }
    state.generatedCoupon?.let {
        AlertDialogWithImageView(
            onDismissRequest = {viewModel.handleCustomerDetailsIntent(CustomerDetailsIntent.ClearCoupon)},
            onConfirmation = {
                val phoneNumber =
                    ("91" + state.customer?.phone) // Country code + number
                val message = "Hi " + state.customer?.name?.uppercase(getDefault()) + ",\n" +
                "Celebrate the upcoming Eid in style – here’s a special coupon just for you! ✨"

                val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = url.toUri()
                    setPackage("com.whatsapp.w4b") // WhatsApp Business
                }

                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback to normal WhatsApp
                    val fallbackIntent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(fallbackIntent)
                }
            },
            dialogTitle = "Coupon Generated",
            bitmap = it
        )
    }
}

@Composable
fun CouponList(list: List<Coupon>, onClick: (id: String) -> Unit) {
    LazyColumn {
        itemsIndexed(
            items = list,
            key = { _, item -> item.id }) { index, coupon ->
            CouponListRow(index, coupon) {
                onClick(coupon.id)
            }
        }
    }
}

@Composable
fun CouponListRow(index: Int, coupon: Coupon, onClick: () -> Unit) {
    val backgroundColor = if (index % 2 == 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.inversePrimary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(Dimens.MEDIUM_PADDING.dp)
    ) {
        Text(
            "Coupon Title: ${coupon.title}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
        Text(
            "Coupon Code: ${coupon.discountCode}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
        Text(
            "Valid Till: ${coupon.discountValidTill.toDateString(Locale.getDefault())}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
        Text(
            "Discount Percentage: ${coupon.discountPercentage}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
        Text(
            "Discount Used: ${coupon.discountAvailedOn}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
        Text(
            "Valid: ${coupon.isActive}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
        Text(
            "Is Synced To Server: ${coupon.isSynced}",
            fontSize = 16.sp,
            fontWeight = Bold
        )
    }
}