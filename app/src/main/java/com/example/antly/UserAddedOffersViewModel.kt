package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.use_case.offer_use_case.DeleteOfferUseCase
import com.example.antly.domain.use_case.offer_use_case.GetAddedOffersUseCase
import com.example.antly.sharedPreferences.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserAddedOffersViewModel @Inject constructor(
    val addedOffersUseCase: GetAddedOffersUseCase,
    val deleteOfferUseCase: DeleteOfferUseCase,
    val sharedPreferencesService: SharedPreferencesService,
) :
    ViewModel() {
    private val _addedOffersState = MutableLiveData<Resource<List<OfferResponse>>>()
    val addedOffersState: MutableLiveData<Resource<List<OfferResponse>>>
        get() = _addedOffersState

    private val _deleteOfferState = MutableLiveData<Resource<List<OfferResponse>>>()
    val deleteOfferState: MutableLiveData<Resource<List<OfferResponse>>>
        get() = _deleteOfferState

    fun getAddedOffers() {
        viewModelScope.launch {
                try {
                    _addedOffersState.postValue(Resource.Loading())
                    val addedOffers = addedOffersUseCase(getLoggedTeacherName())

                    _addedOffersState.postValue(Resource.Success(addedOffers))
                } catch (e: HttpException) {
                    _addedOffersState.postValue(Resource.Error(e.localizedMessage!!))
                } catch (e: IOException) {
                    _addedOffersState.postValue(Resource.Error(e.localizedMessage!!))
                }
            }
    }

    fun deleteOffer(offerId: Int) {
        viewModelScope.launch {
                try {
                    _deleteOfferState.postValue(Resource.Loading())
                    deleteOfferUseCase(offerId)

                    _deleteOfferState.postValue(Resource.Success())
                } catch (e: HttpException) {
                    _deleteOfferState.postValue(Resource.Error(e.localizedMessage!!))
                } catch (e: IOException) {
                    _deleteOfferState.postValue(Resource.Error(e.localizedMessage!!))
                }
            }
    }

    private fun getLoggedTeacherName(): String {
        return sharedPreferencesService.getLoggedUsername()
    }
}