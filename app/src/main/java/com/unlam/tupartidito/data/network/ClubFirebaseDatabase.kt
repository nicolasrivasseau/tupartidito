package com.unlam.tupartidito.data.network

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.common.exist
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.club.Schedule
import com.unlam.tupartidito.data.model.club.Service
import com.unlam.tupartidito.data.model.user.Rent
import kotlinx.coroutines.launch
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
        val usersRef = FirebaseDatabase.getInstance().getReference("clubs")
        val dsClubs = usersRef.get().await().children
        for (dsClub in dsClubs) {
            val club = Club()
            generateClub(dsClub, club)
            listClub.add(club)
        }
        var tmp: Club
        for (x in 0 until listClub.size) {
            for (y in 0 until listClub.size) {
                if (listClub[x].score!!.toFloat() > listClub[y].score!!.toFloat()) {
                    tmp = listClub[y]

                    listClub[y] = listClub[x]
                    listClub[x] = tmp

                }
            }
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
        dataSnapshot.exist("score") { club.score = it as Number }



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

    suspend fun updateRating(rate: Long, idClub: String): Boolean {
        var update = FirebaseDatabase.getInstance().getReference("clubs")
            .child(idClub).child("score").setValue(rate)
        update.await()
        return update.isSuccessful
    }

    suspend fun cancelSchedule(idRent: String, idCLub: String): Boolean {
        var canceledSchedule = FirebaseDatabase.getInstance().getReference("clubs")
            .child(idCLub).child("schedules").child(idRent).child("reserved").setValue(false)
        canceledSchedule.await()
        return canceledSchedule.isSuccessful
    }

    suspend fun reserveSchedule(idRent: String, idCLub: String): Boolean {
        val reserveSchedule = FirebaseDatabase.getInstance().getReference("clubs")
                .child(idCLub).child("schedules").child(idRent).child("reserved").setValue(true)

            reserveSchedule.await()


        return reserveSchedule.isSuccessful
    }

    suspend fun getRent(idRent: String, idClub: String): Rent {
        var rent = Rent()
        val clubsRef = FirebaseDatabase.getInstance().getReference("clubs")
        val dsRent = clubsRef.child(idClub).child("schedules").get().await().child(idRent)
        rent.id_club = idClub
        generateRent(dsRent, rent)
        return rent
    }

    private fun generateRent(dataSnapshot: DataSnapshot, rent: Rent) {
        rent.id_rent = dataSnapshot.key

        dataSnapshot.exist("location") { rent.location = it as String }
        dataSnapshot.exist("price") { rent.price = it as String }
        dataSnapshot.exist("slot") { rent.slot = it as String }
    }
}