package com.unlam.tupartidito.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.google.gson.Gson
import com.unlam.tupartidito.core.observe
import com.unlam.tupartidito.data.model.ErrorCodeQr
import com.unlam.tupartidito.data.model.QrCode.QrCodeJson
import com.unlam.tupartidito.databinding.ActivityDetailBinding
import com.unlam.tupartidito.ui.viewmodel.DetailViewModel


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private  val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val qrCodeString = intent.extras!!.getString(BARCODE_JSON)



        with(viewModel){
            observe(listClubData){currentList ->

                binding.title.text = currentList.Services?.Buffet.toString()
                binding.des1.text = currentList.Schedules[0].Slot
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




    override fun onBackPressed() {
        super.onBackPressed()

        intent = Intent(getApplicationContext(), MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

   private fun goToMain(errorCodeQr : ErrorCodeQr){
       intent = Intent(getApplicationContext(), MainActivity::class.java)
       intent.putExtra(MainActivity.ERROR_QR,true)
       intent.putExtra(MainActivity.ERROR_QR_DESCRIPTION,errorCodeQr.description)
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
       startActivity(intent)
       finish()
   }

    companion object {
        val BARCODE_JSON = "BARCODE_JSON"
    }
}