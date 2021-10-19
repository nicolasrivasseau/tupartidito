package com.unlam.tupartidito.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.unlam.tupartidito.data.model.ErrorCodeQr
import com.unlam.tupartidito.data.model.QrCode.QrCodeJson
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.GetClubUseCase
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _listClubData = MutableLiveData<Club>()
    val listClubData: LiveData<Club> get() = _listClubData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _readErrorQr = MutableLiveData<ErrorCodeQr>()
    val readErrorQr : LiveData<ErrorCodeQr> get() = _readErrorQr

    fun getClubData(qrCodeString : String?) {
        viewModelScope.launch {
            var qrCodeInt : Int = 0


            _isLoading.value = true

            try {
                val gson = Gson()

                val qrCode = gson.fromJson(qrCodeString, QrCodeJson::class.java)




                 GetClubUseCase().invoke(qrCode.id){result ->

                     if (result.id !== null) {
                         _listClubData.value = result
                     }else{
                         _readErrorQr.value = ErrorCodeQr()
                         _readErrorQr.value!!.id = 2
                         _readErrorQr.value!!.description  = "Lista vacia"
                     }
                 }


            }catch (e : Exception){
                _readErrorQr.value = ErrorCodeQr()
                _readErrorQr.value!!.id = 1
                _readErrorQr.value!!.description  = "Codigo QR sin datos validos"
            }



                _isLoading.value = false


        }
    }




}
