package com.unlam.tupartidito.domain.user

import com.unlam.tupartidito.data.UserRepository
import com.unlam.tupartidito.data.model.user.User
import javax.inject.Inject

class GetUserFirebaseUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String): User? {
        return if (username.isNotEmpty()) {
            repository.getUser(username)
        } else {
            null
        }
    }
}