package com.unlam.tupartidito.domain.rent

import android.util.Log
import com.unlam.tupartidito.data.RentRepository
import com.unlam.tupartidito.data.model.user.Rent
import javax.inject.Inject

class GetRentUseCase @Inject constructor(
    private val repository : RentRepository
) {
    suspend operator fun invoke(username:String,rentId:String) : Rent? {
        var rent : Rent?
        rent = repository.getRent(username,rentId)
        if(rent == null ){
            rent = null
        }
        return rent
    }
}

