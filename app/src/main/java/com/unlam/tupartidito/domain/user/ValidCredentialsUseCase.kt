package com.unlam.tupartidito.domain.user

import com.unlam.tupartidito.data.model.user.User

class ValidCredentialsUseCase {
    fun invoke(userFirebase: User?,password:String):Boolean{
        return userFirebase!!.password == password
    }
}