package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.network.ClubFirebaseDatabase



class ClubRepository {

    suspend fun getClubs() : List<Club>{
        return ClubFirebaseDatabase().getClubs()
    }
}