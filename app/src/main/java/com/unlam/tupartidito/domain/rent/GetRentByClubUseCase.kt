package com.unlam.tupartidito.domain.rent

import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.model.user.Rent
import javax.inject.Inject

class GetRentByClubUseCase @Inject constructor(
    private val repository: ClubRepository
) {
    suspend operator fun invoke(idRent: String, idClub: String): Rent {
        return repository.getRent(idRent, idClub)
    }
}