package com.unlam.tupartidito.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.unlam.tupartidito.data.model.ErrorCodeQr
import com.unlam.tupartidito.data.model.QrCode.QrCodeJson
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.club.GetClubUseCase
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _listClubData = MutableLiveData<Club>()
    val listClubData: LiveData<Club> get() = _listClubData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _readErrorQr = MutableLiveData<ErrorCodeQr>()
    val readErrorQr: LiveData<ErrorCodeQr> get() = _readErrorQr

    fun getClubData(qrCodeString: String?) {
        viewModelScope.launch {

            _isLoading.value = true

            try {
                val gson = Gson()

                val qrCode = gson.fromJson(qrCodeString, QrCodeJson::class.java)

                val club = GetClubUseCase().invoke(qrCode.id)

                    if (club.id !== null) {
                        _listClubData.value = club
                    } else {
                        _readErrorQr.value = ErrorCodeQr(id = 2 , description = "Club Inexistente en la base de datos")
                    }


            } catch (e: Exception) {
                _readErrorQr.value = ErrorCodeQr(id = 1 , description = "Codigo QR con datos invalidos")
            }

            _isLoading.value = false


        }
    }


}
