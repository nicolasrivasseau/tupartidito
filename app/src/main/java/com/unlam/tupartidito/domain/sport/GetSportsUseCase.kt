package com.unlam.tupartidito.domain.sport

import com.unlam.tupartidito.data.model.sport.Sport
import com.unlam.tupartidito.data.model.sport.SportProvider
import javax.inject.Inject

class GetSportsUseCase @Inject constructor() {
    operator fun invoke(): List<Sport>{
        return SportProvider.getSports()
    }
}