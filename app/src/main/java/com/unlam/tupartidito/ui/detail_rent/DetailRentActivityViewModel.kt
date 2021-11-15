package com.unlam.tupartidito.ui.detail_rent

import android.util.Log
import androidx.lifecycle.*
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.domain.rent.CancelRentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.unlam.tupartidito.domain.club.GetClubUseCase
import com.unlam.tupartidito.domain.rent.GetRentUseCase


@HiltViewModel
class DetailRentActivityViewModel @Inject constructor(
    private val getClubUseCase: GetClubUseCase,
    private val cancelRentUseCase: CancelRentUseCase,
    private val getRentUseCase: GetRentUseCase,
) :
    ViewModel() {

    var usernameMutable = MutableLiveData<String>()
    var idRentMutable = MutableLiveData<String>()
    fun setUsernameAndIdRent(username: String, idRent: String) {
        usernameMutable.value = username
        idRentMutable.value = idRent
    }
    var idClub = MutableLiveData<String>()
    fun setIdClub(id:String){
        idClub.value = id
    }

    val state = liveData {
        emit(State.Loading)
        emit(State.Success(getRentUseCase(usernameMutable.value.toString(),idRentMutable.value.toString()),getClubUseCase(idClub.value.toString())))
    }

    sealed class State {
        object Loading : State()
        class Success(val rent: Rent?,val club: Club?) : State()
    }


    fun cancelRent(idRent: String, idCLub: String, idUser: String){
        viewModelScope.launch {
            cancelRentUseCase(idRent, idCLub, idUser)!!
        }
    }

    fun reservedRent(username: String, idClub: String?, idRent: String?) {
        viewModelScope.launch {

        }
    }


}