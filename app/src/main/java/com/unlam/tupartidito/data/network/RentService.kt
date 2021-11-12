package com.unlam.tupartidito.data.network

import android.util.Log
import com.unlam.tupartidito.data.model.user.Rent
import javax.inject.Inject

/*
    En esta clase trabajariamos la logica que nos devuelve un tercero, ya se api , webservice o saas.
 */

class RentService @Inject constructor() {
    private val clubFirebaseDatabase = ClubFirebaseDatabase()
    private val userFirebaseDatabase = UserFirebaseDatabase()

    fun getRents() : List<Rent>{
        //tendriamos que llamar a una clase donde se conecte con firebase y devuelva los datos.
        val listRents : List<Rent> = emptyList()
        return listRents
    }

    suspend fun cancelRent(idRent: String, idCLub: String, idUser: String){
        //llamar funcion que elimine renta de cada uno
        //clubFirebaseDatabase
        Log.d("cancelar", "Rent service call cancelrent")

        clubFirebaseDatabase.cancelSchedule(idRent, idCLub)
        //userFirebaseDatabase
        userFirebaseDatabase.cancelRent(idRent, idUser)

    }
}