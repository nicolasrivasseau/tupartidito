package com.unlam.tupartidito.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.unlam.tupartidito.adapter.SportAdapter
import com.unlam.tupartidito.core.observe
import com.unlam.tupartidito.core.toast
import com.unlam.tupartidito.databinding.ActivityMainBinding
import com.unlam.tupartidito.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private  val viewModel: MainViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA)

    private lateinit var adapter: SportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createPermissionsLauncher()


        if(intent.extras?.getBoolean(ERROR_QR) == true){
            toast( intent.extras?.getString(ERROR_QR_DESCRIPTION).toString())
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

        setObservers()
        binding.launchScanner.setOnClickListener { launchCameraClicked() }
        binding.btnMap.setOnClickListener{ launchMapsClicked() }

    }

    private fun createPermissionsLauncher() {
        permissionLauncher =

            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.CAMERA] == true) {
                    launchCamera()
                } else if(permissions[Manifest.permission.CAMERA] == false) {
                    toast("Se necesitan los permisos para lanzar la cámara")
                }else if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                    launchMaps()
                } else if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == false) {
                    toast("Se necesitan los permisos de ubicacion para abrir el mapa.")
                }

            }
    }



    private fun launchCameraClicked() {
        if (arePermissionsGranted()) {
            launchCamera()
        } else {
            askForPermissions("camera")
        }
    }

    private fun launchMapsClicked() {
        if (arePermissionsGranted()) {
            launchMaps()
        } else {
            askForPermissions("maps")
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askForPermissions(activity: String) {
        if(activity == "camera"){
            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        }else if(activity == "maps"){
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }


    }

    private fun launchCamera() {
        startActivity(Intent(this, ScannerActivity::class.java))
    }

    private fun launchMaps() {
        val intent = Intent(binding.root.context, MapActivity::class.java)
        startActivity(intent)
    }

    private fun setObservers(){
        with(viewModel){
            observe(sports){ currentList ->
                adapter = SportAdapter()
                binding.recyclerViewClubs.layoutManager = LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
                binding.recyclerViewClubs.adapter = adapter
                adapter.setDataList(currentList)
                adapter.notifyDataSetChanged()
            }
            getSports()
        }
    }

    companion object {
        val ERROR_QR_DESCRIPTION = "ERROR_QR_DESCRIPTION"
        val ERROR_QR = "ERROR_QR"
    }
}