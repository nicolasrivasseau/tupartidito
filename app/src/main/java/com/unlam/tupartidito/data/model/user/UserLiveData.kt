package com.unlam.tupartidito.data.model.user

data class UserLiveData(
    val isValidSession: Boolean? = null,
    val messageError: Int? = null,
    val user: User? = null
)