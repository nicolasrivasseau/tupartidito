package com.unlam.tupartidito.ui.detail_rent

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.club.GetClubUseCase
import com.unlam.tupartidito.domain.rent.CancelRentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.unlam.tupartidito.common.toast


@HiltViewModel
class DetailRentActivityViewModel @Inject constructor(private val getClubUseCase: GetClubUseCase
,private val cancelRentUseCase: CancelRentUseCase
) :
    ViewModel() {
    private lateinit var myPreferences : SharedPreferences
    private val _clubData = MutableLiveData<Club>()
    val clubData: LiveData<Club> get() = _clubData
    val result = MutableLiveData<Boolean>()

    fun getClubData(idClub: String?) {
        viewModelScope.launch {
            val club = getClubUseCase(idClub.toString())
            if (club.id !== null) {
                _clubData.value = club
            }
        }
    }

    fun cancelRent(idRent: String, idCLub: String, idUser: String){
        viewModelScope.launch {
            Log.d("cancelar", "DetailRentActivityViewModel call cancelrent")
            //result.value =
              val resultado =   cancelRentUseCase(idRent, idCLub, idUser)!!

        }
    }

}