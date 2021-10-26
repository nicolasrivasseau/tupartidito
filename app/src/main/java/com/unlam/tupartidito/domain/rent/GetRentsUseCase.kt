package com.unlam.tupartidito.domain.rent

import com.unlam.tupartidito.data.RentRepository
import com.unlam.tupartidito.data.UserRepository
import com.unlam.tupartidito.data.model.user.Rent
import javax.inject.Inject

/*
Realizamos casos de uso para poder dividir la logica de negocio, esto nos va a permitir aplicar
facilmente inyeccion de dependencias y testeos entre beneficios
 */

class GetRentsUseCase @Inject constructor(
    private val repository : UserRepository
) {
    suspend operator fun invoke(username:String) : List<Rent>? {
        val rents : List<Rent>?
        val user = repository.getUser(username)
        if(user == null ){
            rents = null
        }else{
            rents = user.rents
        }
        return rents
    }
}