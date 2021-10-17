package com.unlam.tupartidito.data

import com.unlam.tupartidito.data.model.User
import com.unlam.tupartidito.data.network.UserService

class UserRepository {
    private val userService = UserService()
     fun getUser(
         username:String,
         onFailure:(message:String)->Unit,
         onSuccess:(user:User)->Unit,
     ) {
        return userService.getUser(username,onFailure,onSuccess)
    }
}