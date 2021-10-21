package com.unlam.tupartidito.data.model

import com.unlam.tupartidito.data.model.rent.Rent

class User{
    var username: String = ""
    var name: String = ""
    var password: String = ""
    var rents : List<Rent> = emptyList()
}