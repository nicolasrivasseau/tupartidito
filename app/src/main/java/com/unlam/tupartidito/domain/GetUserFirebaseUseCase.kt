package com.unlam.tupartidito.domain

import com.unlam.tupartidito.data.UserRepository
import com.unlam.tupartidito.data.model.User

class GetUserFirebaseUseCase {
    private val repository = UserRepository()
    fun invoke(
        username:String,
        onFailure:(message:String)->Unit,
        onSuccess:(user:User)-> Unit
    ) {
        if(username.isNotEmpty()){
            repository.getUser(username,onFailure,onSuccess)
        }else{
            onFailure("Ingrese un usuario/email valido.")
        }
    }
}