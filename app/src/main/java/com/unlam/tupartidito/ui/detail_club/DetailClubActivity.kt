package com.unlam.tupartidito.ui.detail_club

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.data.model.ErrorCodeQr
import com.unlam.tupartidito.databinding.ActivityDetailBinding
import com.unlam.tupartidito.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailClubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private  val viewModel: DetailViewModel by viewModels()
    var clubLocationLat: Double? = null
    var clubLocationLon: Double? = null
    lateinit var qrCodeString: String
    private lateinit var myPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        qrCodeString = intent.extras!!.getString(Constants.BARCODE_JSON).toString()
        myPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        setObservers()
        setEvent()
    }




    private fun setObservers() {

        with(viewModel){

            observe(listClubData){currentList ->
                clubLocationLat = currentList.latitude
                clubLocationLon = currentList.longitude

                binding.title.text = currentList.services?.buffet.toString()
                binding.des1.text = currentList.schedules[0].slot
            }
            observe(isLoading) { currentIsLoading ->
                //todo
            }
            observe(readErrorQr){
                goToMain(it)
            }
            getClubData(qrCodeString)
        }

    }

    private fun setEvent() {
        binding.goToMaps.setOnClickListener { goToMaps(clubLocationLat,clubLocationLon) }
    }

    private fun goToMain(errorCodeQr : ErrorCodeQr){
      val intent = Intent(this, MainActivity::class.java)
       intent.putExtra(Constants.ERROR_QR,true)
       intent.putExtra(Constants.ERROR_QR_DESCRIPTION,errorCodeQr.description)
       intent.putExtra(Constants.MAIN_PARAM,myPreferences.getString("user",""))
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
       startActivity(intent)
       finish()
   }

    private fun goToMaps(lat: Double? , lon: Double?){

        val intentUriNavigation = Uri.parse("google.navigation:q=${lat},${lon}")
        Intent(Intent.ACTION_VIEW, intentUriNavigation).apply {
            setPackage("com.google.android.apps.maps")
            startActivity(this)
        }
    }


}