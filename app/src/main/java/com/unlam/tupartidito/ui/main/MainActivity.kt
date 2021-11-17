package com.unlam.tupartidito.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.unlam.tupartidito.R
import com.unlam.tupartidito.adapter.RentsAdapter
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.checkAndLaunch
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.common.toast
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.databinding.ActivityMainBinding
import com.unlam.tupartidito.ui.detail_club.DetailClubActivity
import com.unlam.tupartidito.ui.login.LoginActivity
import com.unlam.tupartidito.ui.map.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_club.view.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    private val mapViewModel: MapViewModel by viewModels()

    private lateinit var adapterRents: RentsAdapter
    private lateinit var permissionCamera: ActivityResultLauncher<String>
    private lateinit var myPreferences: SharedPreferences
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPermissions()
        validateIntents()
        setEvents()

        createFragment()
    }

    override fun onResume() {
        super.onResume()
        validateIntents()
    }

    private fun validateIntents() {
        if (intent.extras?.getBoolean(Constants.ERROR_QR) == true) {
            toast(intent.extras?.getString(Constants.ERROR_QR_DESCRIPTION).toString())
        }
        if (intent.extras!!.containsKey(Constants.MAIN_PARAM)) {
            val idUser = intent.extras!!.getString(Constants.MAIN_PARAM)
            setObservers()
            mainViewModel.getRents(idUser.toString())
            mainViewModel.getClubs()
        }
    }

    private fun setEvents() {

        binding.launchScanner.setOnClickListener {
            permissionCamera.launch(Manifest.permission.CAMERA)
        }
        binding.logout.setOnClickListener {
            myPreferences.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setObservers() {
        with(mainViewModel) {
            observe(rentsData) { response ->
                adapterRents = RentsAdapter(this@MainActivity)
                binding.recyclerViewRents.layoutManager =
                    LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
                val snapHelper: SnapHelper = PagerSnapHelper()
                if (binding.recyclerViewRents.onFlingListener == null) {
                    snapHelper.attachToRecyclerView(binding.recyclerViewRents)
                }
                binding.recyclerViewRents.setHasFixedSize(true)
                binding.recyclerViewRents.adapter = adapterRents
                adapterRents.setRents(response.rents!!)
                adapterRents.notifyDataSetChanged()
                binding.txtCount.text = adapterRents.itemCount.toString()
            }
            observe(clubsData) { clubs ->
                binding.linearClubs.removeAllViews()
                for (club in clubs) {
                    val child = layoutInflater.inflate(R.layout.item_club, null)
                    child.txtClubLocation.text = club.location
                    child.txtClubRating.text = club.score.toString()
                    val rents = ArrayList<String>()
                    for (r in mainViewModel.rentsData.value?.rents!!) {
                        rents.add(r.id_rent!!)
                    }
                    child.txtClubName.text = club.id.toString().uppercase()
                    child.setOnClickListener {
                        val intent =
                            Intent(binding.root.context, DetailClubActivity::class.java)
                        intent.putExtra(Constants.ID_CLUB, club.id)
                        intent.putExtra(Constants.RENTS, rents)
                        startActivity(intent)
                    }
                    binding.linearClubs.addView(child)
                }
            }
        }
        with(mapViewModel) {
            observe(clubsData) { currentClubs ->
                createMarker(currentClubs)
                zoomLocation()
            }
        }
        mapViewModel.getClubs()
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
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 14f
                        )
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

    //region permission
    private fun setPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCamera =
                checkAndLaunch(Manifest.permission.CAMERA, R.string.permission_miss_camera) {
                    startActivity(Intent(this, ScannerActivity::class.java))
                }
        }
    }
    //endregion

}