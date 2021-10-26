package com.unlam.tupartidito.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.user.UserLiveData
import com.unlam.tupartidito.domain.user.CredentialsNotEmptyUseCase
import com.unlam.tupartidito.domain.user.GetUserFirebaseUseCase
import com.unlam.tupartidito.domain.user.ValidCredentialsUseCase
import com.unlam.tupartidito.domain.user.ValidUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserFirebaseUseCase : GetUserFirebaseUseCase,
    private val credentialsNotEmptyUseCase:CredentialsNotEmptyUseCase,
    private val validUserUseCase:ValidUserUseCase,
    private val validCredentialsUseCase:ValidCredentialsUseCase
) : ViewModel() {

    private val _userData = MutableLiveData<UserLiveData>()
    val userData: LiveData<UserLiveData> get() = _userData

    fun loginSession(username: String, password: String) {
        viewModelScope.launch {
            if (credentialsNotEmptyUseCase(username, password)) {
                val user = getUserFirebaseUseCase(username)
                if (validUserUseCase(user)) {
                    if (validCredentialsUseCase(user, password)) {
                        _userData.value = UserLiveData(true,null,user)
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