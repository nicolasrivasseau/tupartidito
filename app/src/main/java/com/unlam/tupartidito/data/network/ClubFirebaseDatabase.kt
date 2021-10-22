package com.unlam.tupartidito.data.network

import com.unlam.tupartidito.data.model.club.Schedules
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.club.Services
import kotlinx.coroutines.tasks.await

class ClubFirebaseDatabase {
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

    private fun generateClub(ds: DataSnapshot, club: Club) {
        club.id = ds.key
        club.latitude = ds.child("latitude").value as String
        club.longitude = ds.child("longitude").value as String
        club.services = Services()
        club.services!!.buffet = ds.child("services").child("buffet").value as Boolean
        club.services!!.charning_room = ds.child("services").child("charning_room").value as Boolean

        for (dsSchedule in ds.child("schedules").children) {
            val schedule = Schedules()
            schedule.reserved = dsSchedule.child("reserved").value as Boolean
            schedule.slot = dsSchedule.child("slot").value as String
            club.schedules.add(schedule)
        }
    }
}