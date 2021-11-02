package com.unlam.tupartidito.ui.detail_club

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.unlam.tupartidito.data.model.ErrorCodeQr
import com.unlam.tupartidito.data.model.qr.QrCodeJson
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.club.GetClubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getClubUseCase: GetClubUseCase
) : ViewModel() {

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
                val club = getClubUseCase(qrCode.id)
                if (club.id !== null) {
                    _listClubData.value = club
                } else {
                    _readErrorQr.value =
                        ErrorCodeQr(id = 2, description = "Club Inexistente en la base de datos")
                }
            } catch (e: Exception) {
                _readErrorQr.value =
                    ErrorCodeQr(id = 1, description = "Codigo QR con datos invalidos")
            }
            _isLoading.value = false
        }
    }


}
