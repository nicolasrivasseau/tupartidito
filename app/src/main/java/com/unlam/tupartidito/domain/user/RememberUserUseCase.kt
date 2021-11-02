package com.unlam.tupartidito.domain.user

import android.content.SharedPreferences
import javax.inject.Inject

class RememberUserUseCase @Inject constructor() {


     operator fun invoke(username: String?, password: String,myPreferences: SharedPreferences){

         if(!myPreferences.getBoolean("active",false)) {
             with(myPreferences.edit()) {
                 putString("user", username)
                 putString("password", password)
                 putBoolean("active", true)

                 apply()
             }
         }
    }

}