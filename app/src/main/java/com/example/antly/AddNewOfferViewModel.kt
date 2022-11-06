package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.use_case.offer_use_case.AddOfferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddNewOfferViewModel @Inject constructor(val addOfferUseCase: AddOfferUseCase): ViewModel() {

    private val _viewState = MutableLiveData<Resource<OfferResponse>>()
    val viewState: MutableLiveData<Resource<OfferResponse>>
        get() = _viewState

    fun addOffer(offer: Offer) {
        addOfferUseCase(offer).onEach {
            when(it) {
                is Resource.Success -> {
                    _viewState.value = Resource.Success(it.data!!)
                }
                is Resource.Error ->  _viewState.value = Resource.Error("NieudanoVM")
                else -> {
                    println(it)
                    println("INNE")
                }
            }
        }.launchIn(viewModelScope)
    }
}