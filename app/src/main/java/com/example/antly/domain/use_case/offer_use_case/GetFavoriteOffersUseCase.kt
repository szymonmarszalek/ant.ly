package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.data.dto.FavouritesDto
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.repository.OffersRepository
import javax.inject.Inject

class GetFavoriteOffersUseCase @Inject constructor(val offersRepository: OffersRepository) {
    suspend operator fun invoke(username: String): List<FavouritesDto> {
        return offersRepository.getFavoriteOffers(username)
    }
}