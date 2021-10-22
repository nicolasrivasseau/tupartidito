package com.unlam.tupartidito.domain.user

import com.unlam.tupartidito.data.model.user.User

class ValidUserUseCase {
    fun invoke(user:User?):Boolean{
        return user != null
    }
}