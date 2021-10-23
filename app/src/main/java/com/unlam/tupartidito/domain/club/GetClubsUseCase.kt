package com.unlam.tupartidito.domain.club

import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.model.club.Club
import javax.inject.Inject

class GetClubsUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(): List<Club> {
        return clubRepository.getClubs()
    }
}