package com.unlam.tupartidito.data.network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.common.exist
import com.unlam.tupartidito.data.model.user.Rent
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


class RentFirebaseDatabase @Inject constructor() {

    private val clubFirebaseDatabase = ClubFirebaseDatabase()
    private val userFirebaseDatabase = UserFirebaseDatabase()

    suspend fun getRent(username: String, rentId: String) : Rent?{
        val rentRef = FirebaseDatabase.getInstance().getReference("users")
        var rent : Rent?
        try{
            rentRef.child(username).child("rents").child(rentId).get().await().also { ds ->
                rent = Rent()
                if(ds.value != null) {
                    generateRent(ds, rent!!)
                }
            }
        }catch (ex: Exception) {
            rent = null
        }
        return rent
    }

    private fun generateRent(dataSnapshot: DataSnapshot, rent: Rent) {
        rent.id_rent = dataSnapshot.key

        dataSnapshot.exist("id_club") { rent.id_club = it as String }
        dataSnapshot.exist("location") { rent.location = it as String }
        dataSnapshot.exist("price") { rent.price = it as String }
        dataSnapshot.exist("slot") { rent.slot = it as String }
    }

    suspend fun cancelRent(idRent: String, idCLub: String, idUser: String): Boolean{
        var canceledClub = clubFirebaseDatabase.cancelSchedule(idRent, idCLub)
        var canceledUser = userFirebaseDatabase.cancelRent(idRent, idUser)
        if (canceledClub && canceledUser) return true
        return false
    }

    suspend fun createRent(
        idRent: String,
        idCLub: String,
        idUser: String,
        location: String?,
        price: String?,
        slot: String?
    ): Boolean{
        val reserved = clubFirebaseDatabase.reserveSchedule(idRent, idCLub)
        val rentAdded =  userFirebaseDatabase.createRent(idRent, idUser, location, price, slot, idCLub)

        if (reserved && rentAdded) return true
        return false
    }
}