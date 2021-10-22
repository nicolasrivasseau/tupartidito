package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.user.User
import com.unlam.tupartidito.data.network.UserFirebaseDatabase

class UserRepository {
    private val userService = UserFirebaseDatabase()
     suspend fun getUser(username:String) : User?{
        return userService.getUser(username)
    }
}