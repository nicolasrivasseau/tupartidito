package com.unlam.tupartidito.data.network

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.data.model.user.User
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserFirebaseDatabase {

    suspend fun getUser(username: String): User? {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        var user: User?
        try {
            user = usersRef.child(username).get().await().getValue(User::class.java)!!
        } catch (ex: Exception) {
            user = null
            Log.e("TAG",ex.message.toString())
        }
        return user
    }
}