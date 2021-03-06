package com.unlam.tupartidito.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.R
import com.unlam.tupartidito.data.model.user.UserLiveData
import com.unlam.tupartidito.domain.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserFirebaseUseCase: GetUserFirebaseUseCase,
    private val credentialsNotEmptyUseCase: CredentialsNotEmptyUseCase,
    private val validUserUseCase: ValidUserUseCase,
    private val validCredentialsUseCase: ValidCredentialsUseCase,
    private val rememberUserUseCase: RememberUserUseCase,
    private val createUserFirebaseUseCase: CreateUserFirebaseUseCase
) : ViewModel() {

    private val _userData = MutableLiveData<UserLiveData>()
    val userData: LiveData<UserLiveData> get() = _userData

    fun loginSession(username: String, password: String, myPreferences: SharedPreferences) {
        viewModelScope.launch {
            if (credentialsNotEmptyUseCase(username, password)) {
                val user = getUserFirebaseUseCase(username)
                if (validUserUseCase(user)) {
                    if (validCredentialsUseCase(user, password)) {
                        rememberUserUseCase(user?.id,password, myPreferences)
                        _userData.value = UserLiveData(true, null, user)
                    } else {
                        _userData.value = UserLiveData(false, R.string.invalid_credentials)
                    }
                } else {
                    _userData.value = UserLiveData(false, R.string.user_not_exist)
                }
            } else {
                _userData.value = UserLiveData(false, R.string.empty_credentials)
            }
        }
    }
    fun registerUser(username: String, password: String) {
        viewModelScope.launch {
            if (credentialsNotEmptyUseCase(username, password)) {
                val user = getUserFirebaseUseCase(username)
                if (user!!.name.isNullOrBlank()) {
                    createUserFirebaseUseCase(username, password)
                    _userData.value = UserLiveData(false, R.string.user_create)
                } else {
                    _userData.value = UserLiveData(false, R.string.user_exist)
                }
            } else {
                _userData.value = UserLiveData(false, R.string.empty_credentials)
            }
        }
    }

}