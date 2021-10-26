package com.unlam.tupartidito.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.rent.RentsLiveData
import com.unlam.tupartidito.domain.rent.GetRentsUseCase
import com.unlam.tupartidito.domain.rent.RentsIsNotNullUseCase
import com.unlam.tupartidito.domain.sport.GetSportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRentsUseCase: GetRentsUseCase,
    private val getSportsUseCase: GetSportsUseCase,
    private val rentsIsNotNullUseCase: RentsIsNotNullUseCase
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
