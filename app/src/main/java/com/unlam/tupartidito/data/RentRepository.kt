package com.unlam.tupartidito.data

import android.util.Log
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.data.network.RentFirebaseDatabase
import javax.inject.Inject

class RentRepository @Inject constructor(
    private val rentService:RentFirebaseDatabase
){
    suspend fun getRent(username: String, rentId: String): Rent? {
        return rentService.getRent(username,rentId)
    }

    suspend fun cancelRent(idRent: String, idCLub: String, idUser: String): Boolean{
        return rentService.cancelRent(idRent, idCLub,idUser)
    }

    suspend fun createRent(
        idRent: String,
        idCLub: String,
        idUser: String,
        location: String?,
        price: String?,
        slot: String?
    ): Boolean{
        return  rentService.createRent(idRent, idCLub,idUser, location, price, slot)
    }
}