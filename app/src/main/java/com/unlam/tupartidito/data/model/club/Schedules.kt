package com.unlam.tupartidito.data.model.club

import java.io.Serializable

data class Schedules(
    var slot: String = "",
    var reserved: Boolean = false
) :Serializable