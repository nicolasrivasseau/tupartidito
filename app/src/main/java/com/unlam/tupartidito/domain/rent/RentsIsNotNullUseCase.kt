package com.unlam.tupartidito.domain.rent

import com.unlam.tupartidito.data.model.user.Rent
import javax.inject.Inject

class RentsIsNotNullUseCase @Inject constructor() {
    operator fun invoke(rents:List<Rent>?):Boolean{
        return rents != null
    }
}