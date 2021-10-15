package com.unlam.tupartidito.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.unlam.tupartidito.core.observe
import com.unlam.tupartidito.databinding.ActivityMainBinding
import com.unlam.tupartidito.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private  val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // vinculamos nuestro live data con el activity
        with(viewModel){
            observe(listRents){currentList ->
                //todo
            }
            observe(isLoading) { currentIsLoading ->
                //todo
            }
            getRents()
        }

    }
}