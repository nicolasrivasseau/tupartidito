package com.unlam.tupartidito.data.model.club


import com.example.volveralfuturo.data.Schedules
import java.io.Serializable


data class Club(@kotlin.jvm.JvmField var id: String? = null) : Serializable{

    var Services: Services? = null
    var Schedules: MutableList<Schedules> = ArrayList()

}