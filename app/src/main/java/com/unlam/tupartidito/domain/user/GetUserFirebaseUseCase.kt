package com.unlam.tupartidito.domain.user

import com.unlam.tupartidito.data.UserRepository
import com.unlam.tupartidito.data.model.user.User

class GetUserFirebaseUseCase {
    private val repository = UserRepository()
    suspend fun invoke(username:String) : User? {
        val user :User? = if(username.isNotEmpty()){
            repository.getUser(username)
        }else{
            null
        }
        return user
    }
}