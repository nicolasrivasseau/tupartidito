package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.network.ClubService


class ClubRepository {

    fun getListClub(callback: (List<Club>) -> Unit){
      ClubService().getListClub { callback(it) }
    }
}