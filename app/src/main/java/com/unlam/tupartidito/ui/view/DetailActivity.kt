package com.unlam.tupartidito.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.unlam.tupartidito.core.observe
import com.unlam.tupartidito.data.model.ErrorCodeQr
import com.unlam.tupartidito.databinding.ActivityDetailBinding
import com.unlam.tupartidito.ui.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private  val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val qrCodeString = intent.extras!!.getString(BARCODE_JSON)
         var clubLocationLat : Double? = null
         var clubLocationLon : Double? = null


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


            binding.goToMaps.setOnClickListener { goToMaps(clubLocationLat,clubLocationLon) }
        }




    override fun onBackPressed() {
        super.onBackPressed()

        intent = Intent(getApplicationContext(), MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

   private fun goToMain(errorCodeQr : ErrorCodeQr){
       Intent(getApplicationContext(), MainActivity::class.java)
       intent.putExtra(MainActivity.ERROR_QR,true)
       intent.putExtra(MainActivity.ERROR_QR_DESCRIPTION,errorCodeQr.description)
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

    companion object {
        val BARCODE_JSON = "BARCODE_JSON"
    }
}