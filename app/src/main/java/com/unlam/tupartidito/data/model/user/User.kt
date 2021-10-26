package com.unlam.tupartidito.data.model.user

data class User(var id:String?=null,var name:String? = null,var password:String? = null,var rents:ArrayList<Rent> = ArrayList())
