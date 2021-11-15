package com.unlam.tupartidito.domain.rent
import android.util.Log
import com.unlam.tupartidito.data.RentRepository
import javax.inject.Inject


class CreateRentUseCase @Inject constructor(
    private val rentRepository: RentRepository
) {
    suspend operator fun invoke(
        idRent: String,
        idCLub: String,
        idUser: String,
        location: String?,
        price: String?,
        slot: String?
    ): Boolean {
        Log.d("cancelar", "cancel rent use case call cancelrent")


        return rentRepository.createRent(idRent, idCLub, idUser, location, price, slot)
    }

}