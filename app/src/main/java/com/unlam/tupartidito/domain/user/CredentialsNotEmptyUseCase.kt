package com.unlam.tupartidito.domain.user

class CredentialsNotEmptyUseCase {
    fun invoke(username:String,password:String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}