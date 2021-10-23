package com.unlam.tupartidito.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.club.GetClubsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getClubsUseCase:GetClubsUseCase
) : ViewModel() {

    private val _clubsData = MutableLiveData<List<Club>>()
    val clubsData: LiveData<List<Club>> get() = _clubsData

    fun getClubs(){
        viewModelScope.launch {
            val clubs = getClubsUseCase()
            if (clubs != null){
                _clubsData.value = clubs
            }
        }
    }

}