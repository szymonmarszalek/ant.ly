package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.use_case.offer_use_case.DeleteOfferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    val deleteOfferUseCase: DeleteOfferUseCase
): ViewModel() {
    private val _deleteOfferState = MutableLiveData<Resource<List<OfferResponse>>>()
    val deleteOfferState: MutableLiveData<Resource<List<OfferResponse>>>
        get() = _deleteOfferState

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
}