package com.unlam.tupartidito.common
import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes

fun Activity.toast(@StringRes message:Int, length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,message,length).show()
}

fun Activity.toast(message:String, length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,message,length).show()
}