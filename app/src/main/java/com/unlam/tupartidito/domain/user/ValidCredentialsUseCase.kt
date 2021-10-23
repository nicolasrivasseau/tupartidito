package com.unlam.tupartidito.domain.user

import com.unlam.tupartidito.data.model.user.User
import javax.inject.Inject

class ValidCredentialsUseCase @Inject constructor(){
    operator fun invoke(userFirebase: User?,password:String):Boolean{
        return userFirebase!!.password == password
    }
}