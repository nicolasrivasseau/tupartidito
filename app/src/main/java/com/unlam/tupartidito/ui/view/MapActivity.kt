package com.unlam.tupartidito.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.unlam.tupartidito.databinding.ActivityMapBinding
import com.unlam.tupartidito.ui.viewmodel.MapViewModel

class MapActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMapBinding
    private val viewModel : MapViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getClubs()
    }
}