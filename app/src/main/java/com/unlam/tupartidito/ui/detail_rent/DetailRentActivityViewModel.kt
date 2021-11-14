package com.unlam.tupartidito.ui.detail_rent

import android.util.Log
import androidx.lifecycle.*
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.club.GetClubsUseCase
import com.unlam.tupartidito.domain.rent.CancelRentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.unlam.tupartidito.common.toast


@HiltViewModel
class DetailRentActivityViewModel @Inject constructor(private val getClubsUseCase: GetClubsUseCase
,private val cancelRentUseCase: CancelRentUseCase
) :
    ViewModel() {


    val state = liveData {
        emit(State.Loading)
        emit(State.Success(getClubsUseCase()))
    }

    sealed class State {
        object Loading : State()
        class Success(val clubs : List<Club>) : State()

    }


    fun cancelRent(idRent: String, idCLub: String, idUser: String){
        viewModelScope.launch {
            Log.d("cancelar", "DetailRentActivityViewModel call cancelrent")
            //result.value =
              val resultado =   cancelRentUseCase(idRent, idCLub, idUser)!!

        }
    }

}