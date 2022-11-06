package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.common.Resource
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.repository.OffersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllOffersUseCase @Inject constructor(private val offersRepository: OffersRepository) {

    operator fun invoke(): Flow<Resource<List<OfferResponse>>?> = flow {
        try {
            emit(Resource.Loading<List<OfferResponse>>())
            val allOffers = offersRepository.getAllOffers()
            emit(Resource.Success<List<OfferResponse>>(allOffers))
        } catch(e: HttpException) {
            println(e.message())
            emit(Resource.Error<List<OfferResponse>>("BLAD"))
        } catch(e: IOException) {
            emit(e.localizedMessage?.let { Resource.Error<List<OfferResponse>>(it) })
        }
    }
}