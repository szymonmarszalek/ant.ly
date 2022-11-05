package com.example.antly.domain.repository

import com.example.antly.data.dto.Offer

interface OffersRepository {

    suspend fun getAllOffers(): List<Offer>
}