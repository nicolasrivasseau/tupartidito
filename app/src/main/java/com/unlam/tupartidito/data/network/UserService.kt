package com.unlam.tupartidito.data.network

import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.data.model.User

class UserService {

    fun getUser(
        username: String,
        onFailure: (message:String) -> Unit,
        onSuccess: (userSuccess: User) -> Unit,
    ) {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")

        usersRef.child(username).get().addOnSuccessListener {
            val snapshot = it
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
}