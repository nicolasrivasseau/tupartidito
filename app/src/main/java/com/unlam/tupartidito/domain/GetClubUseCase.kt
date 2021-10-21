package com.unlam.tupartidito.domain


import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.model.club.Club


class GetClubUseCase {

    private var repository = ClubRepository()

    suspend operator fun invoke(qrCode: String) : Club {

        var listOfClub = repository.getListClub()

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