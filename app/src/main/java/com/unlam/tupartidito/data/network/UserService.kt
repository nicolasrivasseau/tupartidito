package com.unlam.tupartidito.data.network

import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.data.model.User
import kotlinx.coroutines.tasks.await
import java.util.*

class UserService {

     fun getUser(
        username: String,
        onFailure: (message:String) -> Unit,
        onSuccess: (userSuccess: User) -> Unit,
    ) {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")


        usersRef.child(username).get().addOnSuccessListener { snapshot ->

            if (snapshot!!.exists()) {
                val pass = snapshot.child("Password").value
                val user = User()
                user.password = pass.toString()
                onSuccess(user)
            } else {
                onFailure("No existe usuario.")
            }
        }.addOnFailureListener {
            onFailure("Error al obtener usuarios")
        }
    }

    suspend fun get(username: String) : User?{
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
       return  usersRef.child(username).get().await().getValue(User::class.java)
    }
}