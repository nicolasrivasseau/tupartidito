package com.unlam.tupartidito.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.rent.Rent
import com.unlam.tupartidito.domain.GetRentsUseCase
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

/*
    Generamos nuestros parametros Live data que vamos a vincular con la capa de la view.
    se utiliza encapsulamiento (LiveData) ya que desde la view no se permite modificar parametros del ViewModel.
 */

    private val _listRents = MutableLiveData<List<Rent>>()
    val listRents: LiveData<List<Rent>> get() = _listRents
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getRents() {
        viewModelScope.launch {
            _isLoading.value = true
            val result: List<Rent> = GetRentsUseCase().invoke()
            if (result.isNullOrEmpty()) {
                _listRents.value = result
            }
            _isLoading.value = false
        }
    }
}