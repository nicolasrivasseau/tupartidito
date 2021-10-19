package com.unlam.tupartidito.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.unlam.tupartidito.domain.GetUserFirebaseUseCase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _isValidSession = MutableLiveData<Boolean>()
    val isValidSession: LiveData<Boolean> get() = _isValidSession

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> get() = _messageError

    fun startSession(username: String, password: String) {
        //todo agregarlo todo en una variable los paths
        if(username.isNotEmpty() && password.isNotEmpty()){
        GetUserFirebaseUseCase().invoke(username,
            onFailure = { messageError ->
            _messageError.value = messageError
            _isValidSession.value = false
        }, onSuccess = {
            _isValidSession.value = true
        })
        }else{
            _messageError.value = "Campos vacios."
            _isValidSession.value = false
        }
    }
}