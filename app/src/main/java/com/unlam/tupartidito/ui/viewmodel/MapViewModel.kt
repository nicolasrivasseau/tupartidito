package com.unlam.tupartidito.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.domain.club.GetClubsUseCase
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    fun getClubs(){
        viewModelScope.launch {
            val clubs = GetClubsUseCase().invoke()
            Log.d("TAG", clubs[0].id.toString())
            Log.d("TAG",clubs[1].id.toString())
        }
    }

}