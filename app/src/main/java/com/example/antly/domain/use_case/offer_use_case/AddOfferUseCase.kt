package com.example.antly.domain.use_case.offer_use_case

import com.example.antly.common.Resource
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.repository.OffersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddOfferUseCase @Inject constructor(private val offersRepository: OffersRepository) {
    operator fun invoke(offer: Offer): Flow<Resource<OfferResponse>> = flow {
        try {
            emit(Resource.Loading<OfferResponse>())
            val addedOffer = offersRepository.addNewOffer(offer)
            emit(Resource.Success(addedOffer))
        } catch (e: HttpException) {

        } catch(e: IOException) {

        }
    }
}