package com.unlam.tupartidito.ui.detail_rent

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.domain.club.GetClubUseCase
import com.unlam.tupartidito.domain.rent.CancelRentUseCase
import com.unlam.tupartidito.domain.rent.CreateRentUseCase
import com.unlam.tupartidito.domain.rent.GetRentByClubUseCase
import com.unlam.tupartidito.domain.rent.GetRentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailRentActivityViewModel @Inject constructor(
    private val getClubUseCase: GetClubUseCase,
    private val cancelRentUseCase: CancelRentUseCase,
    private val getRentUseCase: GetRentUseCase,
    private val getRentByClubUseCase: GetRentByClubUseCase,
    private val createRentUseCase: CreateRentUseCase
) :
    ViewModel() {

    var usernameMutable = MutableLiveData<String>()
    var idRentMutable = MutableLiveData<String>()

    private val _isCreated = MutableLiveData<String>()
    val isCreated: LiveData<String> get() = _isCreated

    private val _isCanceled= MutableLiveData<String>()
    val isCanceled: LiveData<String> get() = _isCanceled

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
        emit(State.Success(getRentByClubUseCase(idRent = idRentMutable.value.toString(),idClub = idClub.value.toString()),getClubUseCase(idClub.value.toString())))
    }

    val messageError = MutableLiveData<String>()

    sealed class State {
        object Loading : State()
        class Success(val rent: Rent?,val club: Club?) : State()
    }


    fun cancelRent(idRent: String, idCLub: String, idUser: String){
        viewModelScope.launch {

            if(cancelRentUseCase(idRent, idCLub, idUser)!!){
                _isCanceled.value = "Se cancelo la reserva"
            }else{
                _isCanceled.value = "Error al cancelar"
            }
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
        slot: String?
    ){
        viewModelScope.launch {
            if(createRentUseCase(idRent, idCLub, idUser, location, price, slot)!!){
                _isCreated.value = "Se reservo tu cancha"
            }else{
                _isCreated.value = "Error al generar cancha"
            }
        }
    }

}