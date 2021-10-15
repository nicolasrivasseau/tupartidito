package com.unlam.tupartidito.domain

import com.unlam.tupartidito.data.RentRepository
/*
Realizamos casos de uso para poder dividir la logica de negocio, esto nos va a permitir aplicar
facilmente inyeccion de dependencias y testeos entre beneficios
 */

class GetRentsUseCase {
    private var repository = RentRepository()
    suspend operator fun invoke() = repository.getRents()
}