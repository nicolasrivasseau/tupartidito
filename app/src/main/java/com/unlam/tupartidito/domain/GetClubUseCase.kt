package com.unlam.tupartidito.domain


import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.model.club.Club
import javax.security.auth.callback.Callback


class GetClubUseCase {

    private var repository = ClubRepository()

    suspend operator fun invoke(qrCode : String,callback: (Club) -> Unit){

      repository.getListClub { listOfClub->

          var club : Club

          if(listOfClub.isEmpty()){
              callback(Club())
          }else{
              club = Club()
              listOfClub.forEach {
                  if(it.id == qrCode) {
                      club = it
                  }
              }
              callback(club)
          }}


    }

}