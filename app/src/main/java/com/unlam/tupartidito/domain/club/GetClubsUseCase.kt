package com.unlam.tupartidito.domain.club

import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.model.club.Club

class GetClubsUseCase {
    suspend fun invoke() : List<Club>{
        return ClubRepository().getClubs()
    }
}