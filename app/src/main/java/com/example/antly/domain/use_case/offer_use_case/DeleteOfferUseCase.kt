package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.domain.repository.OffersRepository
import javax.inject.Inject

class DeleteOfferUseCase @Inject constructor(val offersRepository: OffersRepository){
    suspend operator fun invoke(offerId: Int) {
        return offersRepository.deleteOffer(offerId)
    }
}