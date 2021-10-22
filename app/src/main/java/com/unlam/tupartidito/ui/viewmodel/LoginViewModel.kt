package com.unlam.tupartidito.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.user.UserLiveData
import com.unlam.tupartidito.domain.user.CredentialsNotEmptyUseCase
import com.unlam.tupartidito.domain.user.GetUserFirebaseUseCase
import com.unlam.tupartidito.domain.user.ValidCredentialsUseCase
import com.unlam.tupartidito.domain.user.ValidUserUseCase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _userData = MutableLiveData<UserLiveData>()
    val userData: LiveData<UserLiveData> get() = _userData

    fun loginSession(username: String, password: String) {
        viewModelScope.launch {
            if (CredentialsNotEmptyUseCase().invoke(username, password)) {
                val user = GetUserFirebaseUseCase().invoke(username)
                if (ValidUserUseCase().invoke(user)) {
                    if (ValidCredentialsUseCase().invoke(user, password)) {
                        _userData.value = UserLiveData(true)
                    } else {
                        _userData.value = UserLiveData(false, "Credenciales invalidas.")
                    }
                } else {
                    _userData.value = UserLiveData(false, "El usuario no existe.")
                }
            } else {
                _userData.value = UserLiveData(false, "Credenciales vacias.")
            }
        }
    }


}