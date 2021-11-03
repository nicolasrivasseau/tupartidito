package com.unlam.tupartidito.data.model.club

import java.io.Serializable

data class Service(
    var buffet: Boolean = false,
    var charning_room: Boolean = false,
    var gym: Boolean = false,
    var grill: Boolean = false
) : Serializable