package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.repository.OffersRepository
import javax.inject.Inject

class GetOfferByIdUseCase @Inject constructor(val offersRepository: OffersRepository) {
    suspend operator fun invoke(offerId: Int): OfferResponse {
        return offersRepository.getOfferById(offerId)
    }
}