package com.skegworks.mobilepos.pos

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.skegworks.mobilepos.product.ProductListRow
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.utils.Dimens
import java.util.concurrent.Executors

@ExperimentalGetImage
@Composable
fun BarcodeScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: POSViewModel,
    scanCode: ScanCode,
    onBarcodeDetected: () -> Unit,
) {
    println("Item Barcode reader $scanCode")
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasPermission by remember { mutableStateOf(false) }
    var torchEnabled by remember { mutableStateOf(false) }
    var camera: Camera? by remember { mutableStateOf(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                hasPermission = true
            }

            else -> permissionLauncher.launch(Manifest.permission.CAMERA)
        }
        viewModel.handleIntent(POSIntent.ResetError)
    }

    LaunchedEffect(state.value.productFound) {
        if (state.value.productFound) {
            viewModel.handleIntent(POSIntent.UpdateScanState(false))
            onBarcodeDetected()
        }
    }

    if (!hasPermission) {
        Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission required")
        }
        return
    }

    val executor = remember { Executors.newSingleThreadExecutor() }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .width(400.dp)
                .height(250.dp)
        ) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        val scannerOptions = BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                            .build()
                        val scanner = BarcodeScanning.getClient(scannerOptions)

                        var lastValue: String? = null
                        var lastTime = 0L

                        imageAnalyzer.setAnalyzer(executor) { imageProxy ->
                            val mediaImage =
                                imageProxy.image ?: return@setAnalyzer imageProxy.close()
                            val image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            )

                            scanner.process(image)
                                .addOnSuccessListener { barcodes ->
                                    if (barcodes.isNotEmpty()) {
                                        val value = barcodes.first().rawValue
                                        val now = System.currentTimeMillis()
                                        if (value != null && (value != lastValue || now - lastTime > 1500)) {
                                            lastValue = value
                                            lastTime = now
                                            println("Barcode detected: $value")
                                            if (scanCode.scanType == BarcodeScanType.PRODUCT) {
                                                viewModel.getProductForBarcode(value)
                                            } else {
                                                viewModel.getCouponForBarcode(value)
                                            }
                                        }
                                    }
                                }
                                .addOnCompleteListener { imageProxy.close() }
                        }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        cameraProvider.unbindAll()
                        camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )
        }
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SimpleTextField(state.value.searchTerm, "Enter Code") {
                viewModel.handleIntent(POSIntent.UpdateSearchTerm(it))
            }
            Button(onClick = {
                viewModel.handleIntent(POSIntent.SearchItem(scanCode.scanType))
            }) {
                Text("Search")
            }
        }
        Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
        state.value.errorBarcodeScreen?.let {
            Text(it, color = Color.Red)
        }
        Spacer(modifier = Modifier.padding(Dimens.SMALL_PADDING.dp))
        if (scanCode.scanType == BarcodeScanType.PRODUCT) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SMALL_PADDING.dp)) {
                Text("Products", fontWeight = FontWeight.Bold, fontSize = Dimens.MEDIUM_PADDING.sp)
                LazyColumn {
                    itemsIndexed(
                        items = state.value.searchResultProduct,
                        key = { _, item -> item.id }) { index, product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.handleIntent(POSIntent.AddProduct(product))
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ProductListRow(index, product) {
                                //ignore
                            }
                        }
                    }
                }
            }
        }

        if (scanCode.scanType == BarcodeScanType.COUPON) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Coupons", fontWeight = FontWeight.Bold, fontSize = Dimens.MEDIUM_PADDING.sp)
                LazyColumn {
                    items(state.value.searchResultCoupon) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.handleIntent(POSIntent.AddCoupon(it))
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(it.title)
                            Text(it.discountCode)
                        }
                    }
                }
            }
        }

//        IconButton(
//            onClick = {
//                camera?.cameraControl?.enableTorch(!torchEnabled)
//                torchEnabled = !torchEnabled
//            },
//            modifier = Modifier
//                .align(androidx.compose.ui.Alignment.TopEnd)
//                .padding(16.dp)
//        ) {
//            Icon(
//                imageVector = if (torchEnabled)
//                    Icons.Default.FlashOn else Icons.Default.FlashOff,
//                contentDescription = "Toggle Flash"
//            )
//        }
    }
}


enum class BarcodeScanType {
    PRODUCT,
    COUPON
}
