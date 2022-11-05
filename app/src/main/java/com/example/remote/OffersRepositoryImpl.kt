package com.example.remote

import com.example.antly.ApiConnection.AntlyApi
import com.example.antly.data.dto.Offer
import com.example.antly.domain.repository.OffersRepository
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(val api: AntlyApi) : OffersRepository {
    override suspend fun getAllOffers(): List<Offer> {
        return api.getAllOffers()
    }


}