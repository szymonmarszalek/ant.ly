package com.example.antly.domain.repository

import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse

interface OffersRepository {

    suspend fun getAllOffers(): List<OfferResponse>

    suspend fun addNewOffer(offer: Offer): OfferResponse
}