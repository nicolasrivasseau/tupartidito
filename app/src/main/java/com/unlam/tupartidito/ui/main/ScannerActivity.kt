package com.unlam.tupartidito.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.appcompat.app.AppCompatActivity
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.databinding.ActivityScannerBinding
import com.unlam.tupartidito.domain.QrCodeAnalyzerUseCase
import com.unlam.tupartidito.ui.detail_club.DetailClubActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.internal.synchronized

@AndroidEntryPoint
class ScannerActivity : AppCompatActivity() {
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityScannerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Enlazamos el ciclo de vida de las camaras al de la activity
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Generamos el preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }


            var barcodeAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { imageAnalysis ->
                    imageAnalysis.setAnalyzer(cameraExecutor, QrCodeAnalyzerUseCase {

                        cameraProvider.unbindAll()
                        handleQrCode(it)
                    })
                }

            try {
                // Si existia algun provider enlazado al ciclo de vida lo desenlazamos
                cameraProvider.unbindAll()

                // Enlazamos los casos de uso a la camara
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, barcodeAnalyzer
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Fall?? al enlazar la camara", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @Synchronized
    fun handleQrCode(it:String){
        val intent = Intent(this, DetailClubActivity::class.java)
        intent.putExtra(Constants.BARCODE_JSON, it)
        startActivity(intent)
        finish()
    }
}