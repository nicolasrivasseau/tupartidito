package com.unlam.tupartidito.data.network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.core.exist
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
        dataSnapshot.exist("name") { user.name = it as String }
        dataSnapshot.exist("password") { user.password = it as String }

        for (dsRent in dataSnapshot.child("rents").children) {
            val rent = Rent()
            rent.id_rent = dsRent.key
            dsRent.exist("id_club") { rent.id_club = it as String }
            user.rents.add(rent)
        }
    }
}