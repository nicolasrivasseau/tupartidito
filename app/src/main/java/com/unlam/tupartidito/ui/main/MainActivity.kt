package com.unlam.tupartidito.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.unlam.tupartidito.R
import com.unlam.tupartidito.adapter.RentsAdapter
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.checkAndLaunch
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.common.toast
import com.unlam.tupartidito.databinding.ActivityMainBinding
import com.unlam.tupartidito.ui.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_club.view.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapterRents: RentsAdapter
    private lateinit var permissionCamera: ActivityResultLauncher<String>
    private lateinit var permissionLocation: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPermissions()
        setObservers()
        validateIntents()
        setEvents()
    }

    private fun validateIntents() {
        if (intent.extras?.getBoolean(Constants.ERROR_QR) == true) {
            toast(intent.extras?.getString(Constants.ERROR_QR_DESCRIPTION).toString())
        }
        if (intent.extras!!.containsKey(Constants.MAIN_PARAM)) {
            val idUser = intent.extras!!.getString(Constants.MAIN_PARAM)
            viewModel.getRents(idUser.toString())
            viewModel.getClubs()
        }
    }

    private fun setEvents() {
        binding.launchScanner.setOnClickListener {
            permissionCamera.launch(Manifest.permission.CAMERA)
        }
        binding.btnMap.setOnClickListener {
            permissionLocation.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun setObservers() {
        with(viewModel) {
            observe(rentsData) { response ->
                adapterRents = RentsAdapter()
                binding.recyclerViewRents.layoutManager =
                    LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
                val snapHelper: SnapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(binding.recyclerViewRents)
                binding.recyclerViewRents.adapter = adapterRents
                adapterRents.setRents(response.rents!!)
                adapterRents.notifyDataSetChanged()
                binding.txtCount.text = adapterRents.itemCount.toString()
            }
            observe(clubsData) { clubs ->
                for (club in clubs) {
                    val child = layoutInflater.inflate(R.layout.item_club, null)
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
    private fun setPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCamera =
                checkAndLaunch(Manifest.permission.CAMERA, R.string.permission_miss_camera) {
                    startActivity(Intent(this, ScannerActivity::class.java))
                }

            permissionLocation =
                checkAndLaunch(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    R.string.permission_miss_location
                ) {
                    startActivity(Intent(binding.root.context, MapActivity::class.java))
                }
        }
    }

    //endregion

}