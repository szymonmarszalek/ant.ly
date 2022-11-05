package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.common.Resource
import com.example.antly.data.dto.Offer
import com.example.antly.domain.repository.OffersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllOffersUseCase @Inject constructor(val offersRepository: OffersRepository) {
    operator fun invoke(): Flow<Resource<List<Offer>>?> = flow {
        try {
            delay(10000)
            emit(Resource.Loading<List<Offer>>())
            val allOffers = offersRepository.getAllOffers()
            emit(Resource.Success<List<Offer>>(allOffers))
        } catch(e: HttpException) {
            println("BLAD!")
            println(e.message())
            emit(Resource.Error<List<Offer>>("BLAD"))
        } catch(e: IOException) {
            println("BLAD!!")
            emit(e.localizedMessage?.let { Resource.Error<List<Offer>>(it) })
        }
    }
}