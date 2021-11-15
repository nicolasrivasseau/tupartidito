package com.unlam.tupartidito.data.model.club


import java.io.Serializable


data class Club(var id: String? = null) : Serializable {
    var latitude: Double? = null
    var longitude: Double? = null
    var location: String? = null
    var services: Service? = null
    var schedules: ArrayList<Schedule> = ArrayList()
    var url_image: String? = null
    var description: String? = null
    var score: Number? = null
}