package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.use_case.offer_use_case.DeleteOfferUseCase
import com.example.antly.domain.use_case.offer_use_case.EditOfferUseCase
import com.example.antly.domain.use_case.offer_use_case.GetOfferByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EditOfferViewModel @Inject constructor(
    val editOfferUseCase: EditOfferUseCase,
    val getOfferByIdUseCase: GetOfferByIdUseCase
) : ViewModel() {

    private val _viewStateEdit = MutableLiveData<Resource<Boolean>>()
    val viewStateEdit: MutableLiveData<Resource<Boolean>>
        get() = _viewStateEdit

    private val _viewStateOfferById = MutableLiveData<Resource<OfferResponse>>()
    val viewStateOfferById: MutableLiveData<Resource<OfferResponse>>
        get() = _viewStateOfferById

    fun editOffer(offerId: Int, offer: Offer) {
        viewModelScope.launch {
            try {
                _viewStateEdit.postValue(Resource.Loading())
                editOfferUseCase(offerId, offer)
                _viewStateEdit.postValue(Resource.Success())
            } catch (e: HttpException) {
                _viewStateEdit.postValue(Resource.Error(e.localizedMessage))
            } catch (e: IOException) {
                _viewStateEdit.postValue(Resource.Error(e.localizedMessage))
            }
        }
    }

    fun getOfferById(offerId: Int) {
        viewModelScope.launch {
            try {
                _viewStateOfferById.postValue(Resource.Loading())
                val offer = getOfferByIdUseCase(offerId)
                println("OFERTA$offer")
                _viewStateOfferById.postValue(Resource.Success<OfferResponse>(offer))
            } catch (e: HttpException) {
                _viewStateOfferById.postValue(Resource.Error(e.localizedMessage))
            } catch (e: IOException) {
                _viewStateOfferById.postValue(Resource.Error(e.localizedMessage))
            }
        }
    }
}