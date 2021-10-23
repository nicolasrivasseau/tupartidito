package com.unlam.tupartidito.core
import android.app.Activity
import android.widget.Toast

fun Activity.toast(message:String, length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,message,length).show()
}