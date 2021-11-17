package com.unlam.tupartidito.domain.rent

import android.util.Log
import com.unlam.tupartidito.data.RentRepository
import com.unlam.tupartidito.data.model.user.Rent
import javax.inject.Inject

class CheckIfRentExistsInUserUseCase @Inject constructor(
    private val repository : RentRepository
) {
    suspend operator fun invoke(username:String,rentId:String) : Boolean {
        var rentExist: Boolean = false

        val rent : Rent? = repository.getRent(username,rentId)
        if(rent?.id_rent != null ){
            rentExist = true
        }
        return rentExist
    }
}

