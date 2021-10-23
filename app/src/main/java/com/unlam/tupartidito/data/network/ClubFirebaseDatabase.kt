package com.unlam.tupartidito.data.network

import com.unlam.tupartidito.data.model.club.Schedules
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
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
        club.latitude = dataSnapshot.child("latitude").value as Double
        club.longitude = dataSnapshot.child("longitude").value as Double
        club.services = Services()
        club.services!!.buffet = dataSnapshot.child("services").child("buffet").value as Boolean
        club.services!!.charning_room = dataSnapshot.child("services").child("charning_room").value as Boolean

        for (dsSchedule in dataSnapshot.child("schedules").children) {
            val schedule = Schedules()
            schedule.reserved = dsSchedule.child("reserved").value as Boolean
            schedule.slot = dsSchedule.child("slot").value as String
            club.schedules.add(schedule)
        }
    }
}