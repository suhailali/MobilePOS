package com.skegworks.mobilepos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.skegworks.mobilepos.category.CategoryActivity
import com.skegworks.mobilepos.customer.CustomerActivity
import com.skegworks.mobilepos.home.LandingScreen
import com.skegworks.mobilepos.product.ProductActivity
import com.skegworks.mobilepos.sample.SampleListActivity
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import com.skegworks.mobilepos.vendors.VendorActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val list = listOf(
                        "Sale",
                        "Purchase",
                        "POS",
                        "Dashboard",
                        "Report",
                        "Ledger",
                        "Customers",
                        "Vendors",
                        "Expenses",
                        "Coupons",
                        "Product",
                        "Orders",
                        "Settings",
                        "Category"
                    )
                    val context = LocalContext.current
                    LandingScreen(
                        modifier = Modifier.padding(innerPadding),
                        list
                    ) { item ->
                        when (item) {
                            "Vendors" -> {
                                val intent = Intent(context, VendorActivity::class.java)
                                context.startActivity(intent)
                            }

                            "Product" -> {
                                val intent = Intent(context, ProductActivity::class.java)
                                context.startActivity(intent)
                            }

                            "Category" -> {
                                val intent = Intent(context, CategoryActivity::class.java)
                                context.startActivity(intent)
                            }

                            "Customers" -> {
                                val intent = Intent(context, CustomerActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobilePOSTheme {
        Greeting("Android")
    }
}