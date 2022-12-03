package com.example.remote

import com.example.antly.ApiConnection.AntlyApi
import com.example.antly.data.dto.FavoriteOffer
import com.example.antly.data.dto.FavouritesDto
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.repository.OffersRepository
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(val api: AntlyApi) : OffersRepository {

    override suspend fun getAllOffers(): List<OfferResponse> {
        return api.getAllOffers()
    }

    override suspend fun addNewOffer(offer: Offer): OfferResponse {
        return api.postOffer(offer)
    }

    override suspend fun getFilteredOffers(
        range: String,
        subject: String,
        location: String,
    ): List<OfferResponse> {
        return api.getFilteredOffers(subject, range, location)
    }

    override suspend fun getAddedOffers(
        teacherName: String,
    ): List<OfferResponse> {
        return api.getAddedOffers(teacherName)
    }

    override suspend fun deleteOffer(offerId: Int) {
        return api.deleteOffer(offerId)
    }

    override suspend fun editOffer(offerId: Int, offer: Offer) {
        return api.editOffer(offerId, offer)
    }

    override suspend fun getOfferById(offerId: Int)
    :OfferResponse {
        return api.getOfferById(offerId)
    }

    override suspend fun addOfferToFavorites(offerId: Int) {
        return api.addOfferToFavorites(FavoriteOffer(offerId))
    }

    override suspend fun getFavoriteOffers(username: String): List<FavouritesDto> {
        return api.getFavoriteOffers(username)
    }

    override suspend fun deleteFavoriteOffer(username: String, offerId: Int) {
        return api.deleteFromFavorites(offerId,username)
    }
}