package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.data.dto.Offer
import com.example.antly.domain.repository.OffersRepository
import javax.inject.Inject

class EditOfferUseCase @Inject constructor(val offersRepository: OffersRepository) {
    suspend operator fun invoke(offerId: Int, offer: Offer) {
        return offersRepository.editOffer(offerId, offer)
    }
}