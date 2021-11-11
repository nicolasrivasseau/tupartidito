package com.unlam.tupartidito.domain.club

import com.unlam.tupartidito.data.ClubRepository
import com.unlam.tupartidito.data.model.club.Club
import javax.inject.Inject

class SubmitRatingUseCase @Inject constructor(private val repository: ClubRepository) {

    suspend operator fun invoke(rate: Long, idClub: String): Boolean {
        val result = repository.submitRating(rate, idClub)
        return result
    }
}