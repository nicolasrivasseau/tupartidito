package com.unlam.tupartidito.domain.club


import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.model.club.Club
import javax.inject.Inject


class GetClubUseCase @Inject constructor(
    private val repository: ClubRepository
) {
    suspend operator fun invoke(qrCode: String): Club {
        val listOfClub = repository.getClubs()
        var club: Club
        if (listOfClub.isEmpty()) {
            return Club()
        } else {
            club = Club()
            listOfClub.forEach {
                if (it.id == qrCode) {
                    club = it
                }
            }
            return club
        }
    }

}