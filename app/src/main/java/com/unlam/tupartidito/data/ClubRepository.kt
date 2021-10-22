package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.network.ClubFirebaseDatabase
import com.unlam.tupartidito.data.network.ClubService


class ClubRepository {
    suspend fun getListClub(): List<Club> {
        return ClubService().getListClubA()
    }

    suspend fun getClubs() : List<Club>{
        return ClubFirebaseDatabase().getClubs()
    }
}