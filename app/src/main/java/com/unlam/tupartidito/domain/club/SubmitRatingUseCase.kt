package com.unlam.tupartidito.domain.club

import com.unlam.tupartidito.data.ClubRepository
import javax.inject.Inject

class SubmitRatingUseCase @Inject constructor(private val repository: ClubRepository) {

    operator fun invoke(rate: Long, idClub: String) {
        repository.submitRating(rate, idClub)
    }
}