package com.unlam.tupartidito.data.network

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.data.model.club.Club
import kotlinx.coroutines.tasks.await


class ClubService {

    suspend fun getListClubA(): List<Club>{
        val database = getReferenceToDatabase()
        val referenceClubs = database.child("Clubs")
        var listClub: List<Club>
        val listClubSnapshot: MutableList<Club> = mutableListOf()
        var dataSnapshot = referenceClubs.get().await().children

        for(clubSnapshot in dataSnapshot) {

            val club = clubSnapshot.getValue(Club::class.java)
            if (club != null) {
                club.id = clubSnapshot.key
            }
            listClubSnapshot.add(club!!)
        }
        listClub = listClubSnapshot

        return listClub
    }

    private fun getReferenceToDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

}