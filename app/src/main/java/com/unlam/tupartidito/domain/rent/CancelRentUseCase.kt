package com.unlam.tupartidito.domain.rent

import com.unlam.tupartidito.data.RentRepository
import javax.inject.Inject


class CancelRentUseCase @Inject constructor(
    private val rentRepository: RentRepository
) {
    suspend operator fun invoke(idRent: String, idCLub: String, idUser: String): Boolean {

        return rentRepository.cancelRent(idRent, idCLub, idUser)
    }

}