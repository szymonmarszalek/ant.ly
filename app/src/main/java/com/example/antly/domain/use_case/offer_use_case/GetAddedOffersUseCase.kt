package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.repository.OffersRepository
import javax.inject.Inject

class GetAddedOffersUseCase @Inject constructor(val offersRepository: OffersRepository) {
    suspend operator fun invoke(teacherName: String): List<OfferResponse> {
        return offersRepository.getAddedOffers(teacherName)
    }
}