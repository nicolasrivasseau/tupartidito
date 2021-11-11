package com.unlam.tupartidito.ui.detail_club

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.unlam.tupartidito.adapter.TabClubAdapter
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.common.toast
import com.unlam.tupartidito.data.model.ErrorCodeQr
import com.unlam.tupartidito.data.model.qr.QrCodeJson
import com.unlam.tupartidito.databinding.ActivityDetailClubBinding
import com.unlam.tupartidito.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class DetailClubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailClubBinding
    private val viewModel: DetailViewModel by viewModels()
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var idClub: String
    private lateinit var myPreferences: SharedPreferences
    private lateinit var rents: ArrayList<String>
    private val adapter by lazy { TabClubAdapter(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailClubBinding.inflate(layoutInflater)
        myPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        binding.guardarRating.setOnClickListener{puntuarClub()}
        setContentView(binding.root)
        validateIntents()
        setObservers()
    }
    private fun validateIntents() {
        if(intent.extras!!.containsKey(Constants.RENTS)){
            rents = intent.extras!!.getStringArrayList(Constants.RENTS)!!
        } else rents = ArrayList()

        if(intent.extras!!.containsKey(Constants.BARCODE_JSON)){
            val qr = intent.extras!!.getString(Constants.BARCODE_JSON).toString()
            val gson = Gson()
            idClub = gson.fromJson(qr, QrCodeJson::class.java).id
            setTab(idClub)
        }

        if(intent.extras!!.containsKey(Constants.ID_CLUB)){
            idClub = intent.extras!!.getString(Constants.ID_CLUB).toString()
            setTab(idClub)
        }

    }
    private fun puntuarClub(){
        val club = viewModel.clubData.value
        val nuevo_promedio = (club?.puntuacion!!.toFloat() + binding.rbClub.rating)/ 2
        viewModel.submitRating(nuevo_promedio.toLong())
        //        val newRating = binding.rbClub.rating
        //toast(newRating,Toast.LENGTH_SHORT)
    }

    private fun setObservers() {

        with(viewModel) {
            observe(clubData){ club ->
                Picasso.get()
                    .load(club.url_image)
                    .into(binding.ivClub)
                binding.rbClub.rating = club.puntuacion!!.toFloat()!!
            }

//            observe(listClubData) { currentList ->
//                clubLocationLat = currentList.latitude
//                clubLocationLon = currentList.longitude
//
//                binding.title.text = currentList.services?.buffet.toString()
//                binding.des1.text = currentList.schedules[0].slot
//            }
//            observe(isLoading) { currentIsLoading ->
//                //todo
//            }
            
            observe(readErrorQr) {
                goToMain(it)
            }
            observe(result){
                if(it){
                    toast("Calificación enviada", Toast.LENGTH_SHORT)
                } else{
                    toast("Operación fallida", Toast.LENGTH_SHORT)
                }
            }
            getClubData(idClub)
        }

    }

    private fun setTab(idClub : String) {
        with(binding){
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            adapter.setDataClub(idClub, rents)
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout,viewPager){ tab,position ->
                tab.text = listOf("HORARIOS","COMPLEJO")[position]
            }.attach()
        }
    }

//    private fun setEvent() {
//        binding.goToMaps.setOnClickListener { goToMaps(clubLocationLat, clubLocationLon) }
//    }

    private fun goToMain(errorCodeQr: ErrorCodeQr) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.ERROR_QR, true)
        intent.putExtra(Constants.ERROR_QR_DESCRIPTION, errorCodeQr.description)
        intent.putExtra(Constants.MAIN_PARAM, myPreferences.getString("user", ""))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

//    private fun goToMaps(lat: Double?, lon: Double?) {
//
//        val intentUriNavigation = Uri.parse("google.navigation:q=${lat},${lon}")
//        Intent(Intent.ACTION_VIEW, intentUriNavigation).apply {
//            setPackage("com.google.android.apps.maps")
//            startActivity(this)
//        }
//    }


}