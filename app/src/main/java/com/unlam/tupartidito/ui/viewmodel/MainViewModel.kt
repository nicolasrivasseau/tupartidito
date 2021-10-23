package com.unlam.tupartidito.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.domain.GetRentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRentsUseCase:GetRentsUseCase
) : ViewModel() {

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
            val result: List<Rent> = getRentsUseCase()
            if (result.isNullOrEmpty()) {
                _listRents.value = result
            }
            _isLoading.value = false
        }
    }
}