package com.unlam.tupartidito.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.unlam.tupartidito.R
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.databinding.ActivityMapBinding
import com.unlam.tupartidito.ui.detail_club.DetailClubActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap
    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createFragment()
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


        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationManager = LocationServices.getFusedLocationProviderClient(binding.root.context)

        locationManager.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    var location = locationResult.lastLocation
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),14f)
                    )
                    map.isMyLocationEnabled = true
                }
            }, Looper.getMainLooper()
        )
    }


    private fun createMarker(clubs: List<Club>) {
        for (club in clubs) {
            if (club.latitude != null && club.longitude != null) {
                val coordinate = LatLng(club.latitude!!, club.longitude!!)
                map.addMarker(MarkerOptions().position(coordinate).title(club.id))
            }
        }
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        configureMap(map.uiSettings)
        map.setOnMarkerClickListener { currentMaker ->
            val intent = Intent(binding.root.context, DetailClubActivity::class.java)
            intent.putExtra(Constants.BARCODE_JSON, "{id:${currentMaker.title}}")
            startActivity(intent)
            return@setOnMarkerClickListener false
        }
    }

    private fun configureMap(uiSettings: UiSettings?) {
        uiSettings!!.isZoomControlsEnabled = true
        uiSettings.isZoomGesturesEnabled = true
        uiSettings.isCompassEnabled = true
    }



}

