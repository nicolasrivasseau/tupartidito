package com.unlam.tupartidito.domain.user

import com.unlam.tupartidito.data.UserRepository
import com.unlam.tupartidito.data.model.user.User
import javax.inject.Inject

class CreateUserFirebaseUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String, password: String): User? {

            //
        //repository.getUser(username)
            return repository.createUser(username, password)
    }
}