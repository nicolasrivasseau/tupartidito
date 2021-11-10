package com.unlam.tupartidito.ui.detail_rent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.domain.club.GetClubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailRentActivityViewModel @Inject constructor(private val getClubUseCase: GetClubUseCase) :
    ViewModel() {
    private val _clubData = MutableLiveData<Club>()
    val clubData: LiveData<Club> get() = _clubData

    fun getClubData(idClub: String?) {
        viewModelScope.launch {
            val club = getClubUseCase(idClub.toString())
            if (club.id !== null) {
                _clubData.value = club
            }
        }
    }
}