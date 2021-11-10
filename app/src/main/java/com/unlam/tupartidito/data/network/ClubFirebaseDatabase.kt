package com.unlam.tupartidito.data.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.common.exist
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.club.Schedule
import com.unlam.tupartidito.data.model.club.Service
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
    suspend fun getClubsByRate(): List<Club> {
        val listClub: ArrayList<Club> = ArrayList()
        val usersRef = FirebaseDatabase.getInstance().getReference("clubs").orderByChild("puntuacion")
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
        dataSnapshot.exist("latitude") { club.latitude = it as Double }
        dataSnapshot.exist("longitude") { club.longitude = it as Double }
        dataSnapshot.exist("location") { club.location = it as String }
        dataSnapshot.exist("url_image") { club.url_image = it as String }
        dataSnapshot.exist("description") { club.description = it as String }
        dataSnapshot.exist("puntuacion") { club.puntuacion = it as Long }


        club.services = Service()
        dataSnapshot.child("services").exist("buffet") { club.services!!.buffet = it as Boolean }
        dataSnapshot.child("services")
            .exist("charning_room") { club.services!!.charning_room = it as Boolean }
        dataSnapshot.child("services")
            .exist("buffet") { club.services!!.buffet = it as Boolean }
        dataSnapshot.child("services")
            .exist("gym") { club.services!!.gym = it as Boolean }

        for (dsSchedule in dataSnapshot.child("schedules").children) {
            val schedule = Schedule()
            schedule.id = dsSchedule.key
            dsSchedule.exist("reserved") { schedule.reserved = it as Boolean }
            dsSchedule.exist("slot") { schedule.slot = it as String }
            dsSchedule.exist("price") { schedule.price = it as String }
            club.schedules.add(schedule)
        }
    }
}