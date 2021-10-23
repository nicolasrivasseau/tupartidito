package com.unlam.tupartidito.domain.user

import javax.inject.Inject

class CredentialsNotEmptyUseCase @Inject constructor() {
    operator fun invoke(username:String,password:String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}