package com.unlam.tupartidito.data.network

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.data.model.club.Club



class ClubService {

    fun getListClub(callback: (List<Club>) -> Unit){


        lateinit var listClub: List<Club>
        listClub = emptyList()
        //tendriamos que llamar a una clase donde se conecte con firebase y devuelva los datos.
        val database = getReferenceToDatabase()
        val referenceClubs = database.child("Clubs")
        referenceClubs.get().addOnSuccessListener {
            var listClubSnapshot: MutableList<Club> = mutableListOf()

            if (it.exists()) {
                for (clubSnapshot in it.children) {


                    val club = clubSnapshot.getValue(Club::class.java)
                    if (club != null) {
                        club.id = clubSnapshot.key
                    }
                    listClubSnapshot.add(club!!)
                }

                listClub = listClubSnapshot
                callback(listClub)
            }

        }.addOnFailureListener {

            listClub = emptyList()
            callback(listClub)

        }
    }

    private fun getReferenceToDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

}