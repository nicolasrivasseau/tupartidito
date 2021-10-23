package com.unlam.tupartidito.domain.user

import com.unlam.tupartidito.data.model.user.User
import javax.inject.Inject

class ValidUserUseCase @Inject constructor() {
    operator fun invoke(user:User?):Boolean{
        return user != null
    }
}