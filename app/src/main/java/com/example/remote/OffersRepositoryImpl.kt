package com.example.remote

import com.example.antly.ApiConnection.AntlyApi
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
        range: kotlin.String,
        subject: kotlin.String,
        location: kotlin.String,
    ): List<OfferResponse> {
        return api.getFilteredOffers(subject, range, location)
    }
}