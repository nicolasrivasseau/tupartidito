package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.network.ClubFirebaseDatabase
import javax.inject.Inject


class ClubRepository @Inject constructor(
    private val clubFirebaseDatabase : ClubFirebaseDatabase
) {
    suspend fun getClubs() : List<Club>{
        return clubFirebaseDatabase.getClubs()
    }
    suspend fun getClubsByRate() : List<Club>{
        return clubFirebaseDatabase.getClubsByRate()
    }
    fun submitRating(rate: Long, idClub: String){
        clubFirebaseDatabase.updateRating(rate,idClub)
    }

}