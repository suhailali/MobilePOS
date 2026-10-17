package com.skegworks.mobilepos.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil3.Bitmap
import com.skegworks.mobilepos.barcode.ZxingBarcodeGenerator
import com.skegworks.mobilepos.cashcounter.CashCounterActivity
import com.skegworks.mobilepos.category.CategoryActivity
import com.skegworks.mobilepos.customer.CustomerActivity
import com.skegworks.mobilepos.dashboard.DashboardActivity
import com.skegworks.mobilepos.invoice.InvoicesActivity
import com.skegworks.mobilepos.utils.files.PDFFileHandler
import com.skegworks.mobilepos.login.LoginActivity
import com.skegworks.mobilepos.notification.FcmTokenRepository
import com.skegworks.mobilepos.pdf.PdfGeneratorImpl
import com.skegworks.mobilepos.pos.POSActivity
import com.skegworks.mobilepos.print.BLEPrinterConnectActivity
import com.skegworks.mobilepos.product.GenerateBarCodeUseCase
import com.skegworks.mobilepos.product.ProductActivity
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import com.skegworks.mobilepos.vendors.VendorActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var fcmTokenRepository: FcmTokenRepository
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermission()
        viewModel.setupUser()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { events ->
                    when(events) {
                        HomeEvents.NAVIGATE_TO_CUSTOMER -> {
                            val intent = Intent(this@HomeActivity, CustomerActivity::class.java)
                            this@HomeActivity.startActivity(intent)
                        }
                        HomeEvents.NAVIGATE_TO_POS -> {
                            val intent = Intent(this@HomeActivity, POSActivity::class.java)
                            this@HomeActivity.startActivity(intent)
                        }
                        HomeEvents.NAVIGATE_TO_CASH_COUNTER -> {
                            val intent = Intent(this@HomeActivity, CashCounterActivity::class.java)
                            this@HomeActivity.startActivity(intent)
                        }
                    }
                }
            }
        }
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
                                val bundle = bundleOf()
                                bundle.putBoolean("isFromLandingScreen", true)
                                intent.putExtras(bundle)
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
                                    val fileHandler = PDFFileHandler(context)
                                    val pdfFile = fileHandler.writeDocument(pdf, "FileNameSample")
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
                                viewModel.openCashCounter()
                            }

                            "Dashboard" -> {
                                val intent = Intent(context, DashboardActivity::class.java)
                                context.startActivity(intent)
                            }
                            "Invoices" -> {
                                val intent = Intent(context, InvoicesActivity::class.java)
                                context.startActivity(intent)
                            }
                            "BLE Printer" -> {
                                val intent = Intent(context, BLEPrinterConnectActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                registerDevice()
            }
        }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                registerDevice()
            } else {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        } else {
            registerDevice()
        }
    }

    @SuppressLint("HardwareIds")
    private fun registerDevice() {
        val mId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val deviceName = Build.MODEL
        fcmTokenRepository.registerDevice(mId, deviceName)
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