package com.unlam.tupartidito.domain

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class QrCodeAnalyzerUseCase  (val callback: (String) -> Unit) : ImageAnalysis.Analyzer {


    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
        )
        .build()

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {

        if (imageProxy != null) {
            val image =
                InputImage.fromMediaImage(imageProxy.image, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient(options)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    onQrCodeDetected(barcodes) { callback(it) }
                }
                .addOnFailureListener {
                    imageProxy.close()
                }
                .addOnCompleteListener{
                    imageProxy.close()
                }


        }
    }


    private fun onQrCodeDetected(barcodes: MutableList<Barcode>, callback: (String) -> Unit) {

        for (barcode in barcodes) {

            val rawValue = barcode.rawValue

            callback(
                rawValue
            )
        }

    }
}