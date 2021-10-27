package com.unlam.tupartidito.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unlam.tupartidito.R
import com.unlam.tupartidito.adapter.RentsAdapter
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.common.toast
import com.unlam.tupartidito.databinding.ActivityMainBinding
import com.unlam.tupartidito.ui.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_club.view.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity()  {
    private lateinit var binding : ActivityMainBinding

    private  val viewModel: MainViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA)

    private lateinit var adapterRents: RentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPermissionsLauncher()
        setObservers()
        validateIntents()
        setEvents()
    }

    private fun validateIntents() {
        if(intent.extras?.getBoolean(Constants.ERROR_QR) == true){
            toast( intent.extras?.getString(Constants.ERROR_QR_DESCRIPTION).toString())
        }
        if(intent.extras!!.containsKey(Constants.MAIN_PARAM)){
            val idUser = intent.extras!!.getString(Constants.MAIN_PARAM)
            viewModel.getRents(idUser.toString())
            viewModel.getClubs()
        }
    }

    private fun setEvents() {
        binding.launchScanner.setOnClickListener { launchCameraClicked() }
        binding.btnMap.setOnClickListener{ launchMapsClicked() }
    }

    private fun setObservers(){
        with(viewModel){
            observe(rentsData){ response ->
                adapterRents = RentsAdapter()
                binding.recyclerViewRents.layoutManager = LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
                binding.recyclerViewRents.adapter = adapterRents
                adapterRents.setRents(response.rents!!)
                adapterRents.notifyDataSetChanged()
                binding.txtCount.text = adapterRents.itemCount.toString()
            }
            observe(clubsData){clubs ->
                for(club in clubs){
                    val child = layoutInflater.inflate(R.layout.item_club,null)
                    child.txtClubLocation.text = club.location
                    child.txtClubName.text = club.id.toString().uppercase()
                    child.setOnClickListener {
                        toast("test${child.txtClubName.text.toString()}")
                    }
                    binding.linearClubs.addView(child)
                }
            }
        }
    }

    //region permission
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
    //endregion

}