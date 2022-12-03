package com.example.antly.domain.repository

import com.example.antly.data.dto.FavouritesDto
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse

interface OffersRepository {

    suspend fun getAllOffers(): List<OfferResponse>

    suspend fun addNewOffer(offer: Offer): OfferResponse

    suspend fun getFilteredOffers(
        range: String,
        subject: String,
        location: String,
    ): List<OfferResponse>

    suspend fun getAddedOffers(
        teacherName: String,
    ): List<OfferResponse>

    suspend fun deleteOffer(
        offerId: Int,
    )

    suspend fun editOffer(
        offerId: Int,
        offer: Offer
    )

    suspend fun getOfferById(
        offerId: Int,
    ) : OfferResponse

    suspend fun addOfferToFavorites(
        offerId: Int,
    )

    suspend fun getFavoriteOffers(
        username: String,
    ): List<FavouritesDto>

    suspend fun deleteFavoriteOffer(
        username: String,
        offerId: Int
    )
}