package com.unlam.tupartidito.domain.rent

import android.util.Log
import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.RentRepository
import com.unlam.tupartidito.data.model.club.Club
import javax.inject.Inject


class CancelRentUseCase @Inject constructor(
    private val rentRepository: RentRepository
) {
    suspend operator fun invoke(idRent: String, idCLub: String, idUser: String): Boolean {
        Log.d("cancelar", "cancel rent use case call cancelrent")

        rentRepository.cancelRent(idRent, idCLub, idUser)
        return true
    }

}