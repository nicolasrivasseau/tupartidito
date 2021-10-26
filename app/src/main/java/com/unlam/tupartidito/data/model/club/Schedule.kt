package com.unlam.tupartidito.data.model.club

import java.io.Serializable

data class Schedule(
    var id:String? = null,
    var slot: String = "",
    var reserved: Boolean = false,
    var price: String? = null
) :Serializable