package com.unlam.tupartidito.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.unlam.tupartidito.core.observe
import com.unlam.tupartidito.databinding.ActivityMainBinding
import com.unlam.tupartidito.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private  val viewModel: MainViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createPermissionsLauncher()

        if(intent.extras?.getBoolean(ERROR_QR) == true){
            Snackbar.make(binding.root, intent.extras?.getString(ERROR_QR_DESCRIPTION).toString(), Snackbar.LENGTH_SHORT)
                .show()
        }

        // vinculamos nuestro live data con el activity
        with(viewModel){
            observe(listRents){currentList ->
                //todo
            }
            observe(isLoading) { currentIsLoading ->
                if(currentIsLoading){

                }else{

                }
            }
            getRents()
        }

        binding.launchScanner.setOnClickListener { launchCameraClicked() }
        binding.btnMap.setOnClickListener{
            val intent = Intent(binding.root.context, MapActivity::class.java)
            startActivity(intent)
        }

    }





    private fun createPermissionsLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.CAMERA] == true) {
                    launchCamera()
                } else {
                    Toast.makeText(
                        this,
                        "Se necesitan los permisos para lanzar la cámara",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun launchCameraClicked() {
        if (arePermissionsGranted()) {
            launchCamera()
        } else {
            askForPermissions()
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askForPermissions() {
        permissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun launchCamera() {
        startActivity(Intent(this, ScannerActivity::class.java))
    }

    companion object {
        val ERROR_QR_DESCRIPTION = "ERROR_QR_DESCRIPTION"
        val ERROR_QR = "ERROR_QR"
    }
}