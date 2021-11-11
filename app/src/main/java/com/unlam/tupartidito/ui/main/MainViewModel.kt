package com.unlam.tupartidito.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.rent.RentsLiveData
import com.unlam.tupartidito.domain.club.GetClubsByRateUseCase
import com.unlam.tupartidito.domain.club.GetClubsUseCase
import com.unlam.tupartidito.domain.rent.GetRentsUseCase
import com.unlam.tupartidito.domain.rent.RentsIsNotNullUseCase
import com.unlam.tupartidito.domain.sport.GetSportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRentsUseCase: GetRentsUseCase,
    private val rentsIsNotNullUseCase: RentsIsNotNullUseCase,
    private val getClubsUseCase: GetClubsUseCase,
    private val getClubsByRateUseCase: GetClubsByRateUseCase
) : ViewModel() {
    
    private val _rentsData = MutableLiveData<RentsLiveData>()
    val rentsData: LiveData<RentsLiveData> get() = _rentsData

    fun getRents(username:String) {
        viewModelScope.launch {
            val rents = getRentsUseCase(username)
            if(rentsIsNotNullUseCase(rents)){
                _rentsData.value = RentsLiveData(true,null,rents)
            }else{
                _rentsData.value = RentsLiveData(false,"rents is null",null)
            }
        }
    }

    private val _clubsData = MutableLiveData<List<Club>>()
    val clubsData: LiveData<List<Club>> get() = _clubsData

    fun getClubs(){
        viewModelScope.launch {
            //val clubs = getClubsUseCase()
            val clubs = getClubsByRateUseCase()
            if (clubs != null){
                _clubsData.value = clubs
            }
        }
    }

}



//
//    private val _sports = MutableLiveData<List<Sport>>()
//    val sports: LiveData<List<Sport>> get() = _sports
//
//    fun getSports(){
//        viewModelScope.launch {
//            val sports = getSportsUseCase()
//            if(sports.count() != 0){
//                _sports.value = sports
//            }
//        }
//    }
