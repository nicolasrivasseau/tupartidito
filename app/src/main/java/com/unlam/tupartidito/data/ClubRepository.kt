package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.user.Rent
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
    suspend fun getRent(idRent:String,idClub: String): Rent {
        return clubFirebaseDatabase.getRent(idRent,idClub)
    }
    suspend fun submitRating(rate: Long, idClub: String): Boolean{
        return clubFirebaseDatabase.updateRating(rate,idClub)
    }
}