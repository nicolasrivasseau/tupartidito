package com.unlam.tupartidito.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var myPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        checkIfTheUserIsRemembered()
        finish()
    }


    private fun checkIfTheUserIsRemembered() {

            if (myPreferences.getBoolean("active", false)) {
                startActivity(Intent(this, MainActivity::class.java).apply { putExtra(Constants.MAIN_PARAM, myPreferences.getString("user","")) })
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }

    }
}