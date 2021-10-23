package com.unlam.tupartidito.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.unlam.tupartidito.R
import com.unlam.tupartidito.core.observe
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.databinding.ActivityMapBinding
import com.unlam.tupartidito.ui.viewmodel.MapViewModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap
    private val viewModel: MapViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPermissionsLauncher()
        validatePermissions()
        setObserver()

        viewModel.getClubs()
    }

    private fun setObserver() {
        with(viewModel) {
            observe(clubsData) { currentClubs ->
                createMarker(currentClubs)
                zoomLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun zoomLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(binding.root.context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude,it.longitude),14f))
        }
    }


private fun createMarker(clubs: List<Club>) {
    for (club in clubs) {
        val coordinate = LatLng(club.latitude!!, club.longitude!!)
        map.addMarker(MarkerOptions().position(coordinate).title(club.id))
    }
}

private fun createFragment() {
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
}

override fun onMapReady(googleMap: GoogleMap?) {
    map = googleMap!!
    map.setOnMarkerClickListener{currentMaker ->
        val intent = Intent(binding.root.context,DetailActivity::class.java)
        intent.putExtra(DetailActivity.BARCODE_JSON, "{id:${currentMaker.title}}")
        startActivity(intent)
        return@setOnMarkerClickListener false
    }
}

private fun validatePermissions() {
    if (arePermissionsGranted()) {
        createFragment()
    } else {
        askForPermissions()
    }
}

private fun createPermissionsLauncher() {
    permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                createFragment()
            } else {
                Toast.makeText(
                    this,
                    "Checkear permisos ubicacion.",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
}

