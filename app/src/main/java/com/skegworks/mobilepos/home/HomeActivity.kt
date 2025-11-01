package com.skegworks.mobilepos.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import coil3.Bitmap
import com.skegworks.mobilepos.barcode.ZxingBarcodeGenerator
import com.skegworks.mobilepos.category.CategoryActivity
import com.skegworks.mobilepos.customer.CustomerActivity
import com.skegworks.mobilepos.utils.files.FileHandlerImpl
import com.skegworks.mobilepos.login.LoginActivity
import com.skegworks.mobilepos.pdf.PdfGeneratorImpl
import com.skegworks.mobilepos.pos.POSActivity
import com.skegworks.mobilepos.product.GenerateBarCodeUseCase
import com.skegworks.mobilepos.product.ProductActivity
import com.skegworks.mobilepos.splash.SplashViewModel
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import com.skegworks.mobilepos.vendors.VendorActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.getUserRole()
        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val context = LocalContext.current
                    LandingScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel
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

                            "Ledger" -> {
                                val barcodeUseCase = GenerateBarCodeUseCase(ZxingBarcodeGenerator())
                                runBlocking {

                                    val listOfBitmaps = mutableListOf<Bitmap>()
                                    for (i in 0..39) {
                                        val bitmap = barcodeUseCase.invoke("12345678901${i}")
                                        listOfBitmaps.add(bitmap)
                                    }
                                    println("List of bitmaps generated")
                                    val pdfGenerator = PdfGeneratorImpl()
                                    val pdf = pdfGenerator.generatePdf(listOfBitmaps)
                                    println("PDF File generated")
                                    val fileHandler = FileHandlerImpl(context)
                                    val pdfFile = fileHandler.writePdfDocument(pdf)
                                    println("PDF File written at: $pdfFile")
                                }
                            }

                            "Login" -> {
                                // For testing purpose only
                                val intent = Intent(context, LoginActivity::class.java)
                                val bundle = bundleOf()
                                bundle.putBoolean("isCreateUser", false)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }

                            "CreateUser" -> {
                                // For testing purpose only
                                val intent = Intent(context, LoginActivity::class.java)
                                val bundle = bundleOf()
                                bundle.putBoolean("isCreateUser", true)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }

                            "POS" -> {
                                val intent = Intent(context, POSActivity::class.java)
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