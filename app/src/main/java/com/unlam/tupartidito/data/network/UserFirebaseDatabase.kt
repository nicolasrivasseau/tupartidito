package com.unlam.tupartidito.data.network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
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
            Log.e("TAG", ex.message.toString())
        }
        return user
    }

    private fun generateUser(ds: DataSnapshot, user: User) {
        user.name = ds.child("name").getValue(String::class.java)
        user.password = ds.child("password").getValue(String::class.java)

        for (dsRent in ds.child("rents").children) {
            val rent = Rent()
            rent.id_rent = dsRent.key
            rent.id_club = dsRent.child("id_club").value as String
            user.rents.add(rent)
        }
    }
}