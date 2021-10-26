package com.unlam.tupartidito.data.network

import com.unlam.tupartidito.data.model.club.Schedules
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.common.exist
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.club.Services
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClubFirebaseDatabase @Inject constructor() {
    suspend fun getClubs(): List<Club> {
        val listClub: ArrayList<Club> = ArrayList()
        val usersRef = FirebaseDatabase.getInstance().getReference("clubs")
        val dsClubs = usersRef.get().await().children
            for (dsClub in dsClubs) {
                val club = Club()
                generateClub(dsClub, club)
                listClub.add(club)
            }
        return listClub
    }

    private fun generateClub(dataSnapshot: DataSnapshot, club: Club) {
        club.id = dataSnapshot.key
        dataSnapshot.exist("latitude"){club.latitude= it as Double}
        dataSnapshot.exist("longitude"){club.longitude= it as Double}

        club.services = Services()
        dataSnapshot.child("services").exist("buffet"){club.services!!.buffet = it as Boolean}
        dataSnapshot.child("services").exist("charning_room"){club.services!!.charning_room = it as Boolean}

        for (dsSchedule in dataSnapshot.child("schedules").children) {
            val schedule = Schedules()
            dsSchedule.exist("reserved"){schedule.reserved = it as Boolean}
            dsSchedule.exist("slot"){schedule.slot = it as String}
            club.schedules.add(schedule)
        }
    }
}