package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.use_case.offer_use_case.GetAllOffersUseCase
import com.example.antly.domain.use_case.offer_use_case.GetFilteredOffersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getAllOffersUseCase: GetAllOffersUseCase,
    val getFilteredOffersUseCase: GetFilteredOffersUseCase,
) : ViewModel() {

    private val _viewAllOfferState = MutableLiveData<Resource<List<OfferResponse>>>()
    val viewAllOfferState: MutableLiveData<Resource<List<OfferResponse>>>
        get() = _viewAllOfferState

    fun getAllOffers() {
        getAllOffersUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    _viewAllOfferState.value = Resource.Success(it.data!!)
                }
                is Resource.Error -> _viewAllOfferState.value = Resource.Error("NieudanoVM")
                else -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun getFilteredOffers(range: kotlin.String, subject: kotlin.String, location: kotlin.String) {
        getFilteredOffersUseCase(range, subject, location).onEach {
            println("USECASEOPEN")
            when (it) {
                is Resource.Success -> {
                    _viewAllOfferState.value = Resource.Success(it.data!!)
                }
                is Resource.Error -> _viewAllOfferState.value = Resource.Error("NieudanoVM")
                is Resource.Loading -> _viewAllOfferState.value = Resource.Loading()
            }
        }.launchIn(viewModelScope)
    }
}