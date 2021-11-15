package com.unlam.tupartidito.ui.detail_rent

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.domain.rent.CancelRentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.unlam.tupartidito.domain.club.GetClubUseCase
import com.unlam.tupartidito.domain.rent.CreateRentUseCase
import com.unlam.tupartidito.domain.rent.GetRentUseCase


@HiltViewModel
class DetailRentActivityViewModel @Inject constructor(
    private val getClubUseCase: GetClubUseCase,
    private val cancelRentUseCase: CancelRentUseCase,
    private val getRentUseCase: GetRentUseCase,
    private val createRentUseCase: CreateRentUseCase
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
    fun createRent(
        idRent: String,
        idCLub: String,
        idUser: String,
        location: String?,
        price: String?,
        slot: String?,
        context: Context?
    ){
        viewModelScope.launch {
            val resultado =  createRentUseCase(idRent, idCLub, idUser, location, price, slot)!!

            if (resultado){
                Toast.makeText(context, "Reservado exitosamente", Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(context, "Reserva fallida", Toast.LENGTH_LONG).show()
            }
        }
    }

}