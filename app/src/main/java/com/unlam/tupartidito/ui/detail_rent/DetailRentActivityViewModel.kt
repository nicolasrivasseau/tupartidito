package com.unlam.tupartidito.ui.detail_rent

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.domain.club.GetClubUseCase
import com.unlam.tupartidito.domain.rent.CancelRentUseCase
import com.unlam.tupartidito.domain.rent.CreateRentUseCase
import com.unlam.tupartidito.domain.rent.GetRentByClubUseCase
import com.unlam.tupartidito.domain.rent.CheckIfRentExistsInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailRentActivityViewModel @Inject constructor(
    private val getClubUseCase: GetClubUseCase,
    private val cancelRentUseCase: CancelRentUseCase,
    private val checkIfRentExistsInUserUseCase: CheckIfRentExistsInUserUseCase,
    private val getRentByClubUseCase: GetRentByClubUseCase,
    private val createRentUseCase: CreateRentUseCase
) :
    ViewModel() {
    var usernameMutable = MutableLiveData<String>()
    var idRentMutable = MutableLiveData<String>()


    private val _reservedByUser= MutableLiveData<Boolean>()
    val reservedByUser: LiveData<Boolean> get() = _reservedByUser


    fun getRentUser(){
        viewModelScope.launch {
            val isReservedForUser = checkIfRentExistsInUserUseCase(usernameMutable.value.toString(), idRentMutable.value.toString())
            _reservedByUser.value = isReservedForUser
        }
    }

    fun setUsernameAndIdRent(username: String,idRent: String) {
        usernameMutable.value = username
        idRentMutable.value = idRent
    }
    var idClub = MutableLiveData<String>()
    fun setIdClub(id:String){
        idClub.value = id
    }

    val state = liveData {
        emit(State.Loading)
        emit(State.Success(getRentByClubUseCase(idRent = idRentMutable.value.toString(),idClub = idClub.value.toString()),getClubUseCase(idClub.value.toString())))
    }

    sealed class State {
        object Loading : State()
        class Success(val rent: Rent?,val club: Club?) : State()
    }


    fun cancelRent(idRent: String, idCLub: String, idUser: String){
        viewModelScope.launch {
            cancelRentUseCase(idRent, idCLub, idUser)
        }
    }

    fun createRent(
        idRent: String,
        idCLub: String,
        idUser: String,
        location: String?,
        price: String?,
        slot: String?
    ) {
        viewModelScope.launch {
            if(createRentUseCase(idRent, idCLub, idUser, location, price, slot)){
                _reservedByUser.value = true
            }
        }
    }

}