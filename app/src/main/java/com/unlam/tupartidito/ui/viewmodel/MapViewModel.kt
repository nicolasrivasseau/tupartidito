package com.unlam.tupartidito.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.club.GetClubsUseCase
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val _clubsData = MutableLiveData<List<Club>>()
    val clubsData: LiveData<List<Club>> get() = _clubsData
    fun getClubs(){
        viewModelScope.launch {
            val clubs = GetClubsUseCase().invoke()
            if (clubs != null){
                _clubsData.value = clubs
            }
        }
    }

}