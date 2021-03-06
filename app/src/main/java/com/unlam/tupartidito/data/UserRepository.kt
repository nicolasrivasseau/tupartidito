package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.user.User
import com.unlam.tupartidito.data.network.UserFirebaseDatabase
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService : UserFirebaseDatabase
    ) {

     suspend fun getUser(username:String) : User?{
        return userService.getUser(username)
    }

    suspend fun createUser(username: String, password: String): User?{
        return userService.createUser(username, password)
    }
}