package com.unlam.tupartidito.data

import android.util.Log
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.data.network.RentService
import javax.inject.Inject

class RentRepository @Inject constructor(
    private val rentService:RentService
){
    fun getRents(): List<Rent> {
        return rentService.getRents()
    }

    suspend fun cancelRent(idRent: String, idCLub: String, idUser: String): Boolean{
        Log.d("cancelar", "rent repository call cancelrent")

        rentService.cancelRent(idRent, idCLub,idUser)
        return true
    }
}