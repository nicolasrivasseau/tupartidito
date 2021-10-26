package com.unlam.tupartidito.data.model.rent

import com.unlam.tupartidito.data.model.user.Rent

data class RentsLiveData(
    val isValid: Boolean? = null,
    val messageError: String? = null,
    val rents: List<Rent>? = null
)
