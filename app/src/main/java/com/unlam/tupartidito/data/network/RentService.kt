package com.unlam.tupartidito.data.network

import com.unlam.tupartidito.data.model.rent.Rent

/*
    En esta clase trabajariamos la logica que nos devuelve un tercero, ya se api , webservice o saas.
 */

class RentService {
    fun getRents() : List<Rent>{
        //tendriamos que llamar a una clase donde se conecte con firebase y devuelva los datos.
        val listRents : List<Rent> = emptyList()
        return listRents
    }
}