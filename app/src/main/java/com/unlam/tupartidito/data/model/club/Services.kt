package com.unlam.tupartidito.data.model.club

import java.io.Serializable

data class Services(
    var buffet: Boolean = false,
    var charning_room: Boolean = false
) : Serializable