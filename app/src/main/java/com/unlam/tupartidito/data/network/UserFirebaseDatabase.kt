package com.unlam.tupartidito.data.network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.common.exist
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.data.model.user.User
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class UserFirebaseDatabase @Inject constructor() {

    suspend fun getUser(username: String): User? {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        var user: User?
        try {
            usersRef.child(username).get().await().also { ds ->
                user = User()
                generateUser(ds, user!!)
            }
        } catch (ex: Exception) {
            user = null
        }
        return user
    }

    private fun generateUser(dataSnapshot: DataSnapshot, user: User) {
        user.id = dataSnapshot.key
        dataSnapshot.exist("name") { user.name = it as String }
        dataSnapshot.exist("password") { user.password = it as String }

        for (dsRent in dataSnapshot.child("rents").children) {
            val rent = Rent()
            rent.id_rent = dsRent.key
            dsRent.exist("id_club") { rent.id_club = it as String }
            dsRent.exist("location") { rent.location = it as String }
            dsRent.exist("price") { rent.price = it as String }
            dsRent.exist("slot") { rent.slot = it as String }
            user.rents.add(rent)
        }
    }

    suspend fun cancelRent(idRent: String, idUser: String): Boolean{
        var cancelSchedule = FirebaseDatabase.getInstance().getReference("users")
          .child(idUser).child("rents").child(idRent).removeValue()

        cancelSchedule.await().also {return cancelSchedule.isSuccessful  }


    }

    suspend fun createRent(
        idRent: String,
        idUser: String,
        location: String?,
        price: String?,
        slot: String?,
        idCLub: String
    ): Boolean{
        var clubSuccess = FirebaseDatabase.getInstance().getReference("users")
            .child(idUser).child("rents").child(idRent).child("id_club").setValue(idCLub)
        clubSuccess.await()
        var locationSuccess = FirebaseDatabase.getInstance().getReference("users")
            .child(idUser).child("rents").child(idRent).child("location").setValue(location)
        locationSuccess.await()
        var priceSuccess = FirebaseDatabase.getInstance().getReference("users")
            .child(idUser).child("rents").child(idRent).child("price").setValue(price)
        priceSuccess.await()
        var slotSuccess = FirebaseDatabase.getInstance().getReference("users")
            .child(idUser).child("rents").child(idRent).child("slot").setValue(slot)
        slotSuccess.await()

        if(clubSuccess.isSuccessful && locationSuccess.isSuccessful && priceSuccess.isSuccessful && slotSuccess.isSuccessful) return true

        return false
    }

    suspend fun createUser(username: String, password: String): User?{
        FirebaseDatabase.getInstance().getReference("users").child(username).child("name").setValue( username)
        FirebaseDatabase.getInstance().getReference("users").child(username).child("password").setValue( password)

        return getUser(username)
    }
}