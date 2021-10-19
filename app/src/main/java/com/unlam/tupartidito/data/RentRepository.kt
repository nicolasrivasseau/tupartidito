package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.rent.Rent
import com.unlam.tupartidito.data.network.RentService

class RentRepository {
    fun getRents(): List<Rent> {
        return RentService().getRents()
    }
}