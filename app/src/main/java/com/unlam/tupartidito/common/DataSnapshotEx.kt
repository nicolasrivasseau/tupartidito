package com.unlam.tupartidito.common

import com.google.firebase.database.DataSnapshot

fun DataSnapshot.exist(nameChild:String,onSuccess:(Any)->Unit){
    if (this.child(nameChild).exists()){
        val value = this.child(nameChild).value as Any
        onSuccess(value)

    }
}