package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.data.network.RentService
import javax.inject.Inject

class RentRepository @Inject constructor(
    private val rentService:RentService
){
    fun getRents(): List<Rent> {
        return rentService.getRents()
    }
}