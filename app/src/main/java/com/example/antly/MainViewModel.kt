package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.FavouritesDto
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.use_case.offer_use_case.*
import com.example.antly.sharedPreferences.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getAllOffersUseCase: GetAllOffersUseCase,
    val getFilteredOffersUseCase: GetFilteredOffersUseCase,
    val addToFavoritesUseCase: AddToFavoritesUseCase,
    val getFavoriteOffersUseCase: GetFavoriteOffersUseCase,
    val deleteOfferFromFavoritesUseCase: DeleteOfferFromFavoritesUseCase,
    val sharedPreferencesService: SharedPreferencesService,
) : ViewModel() {

    private val _viewAllOfferState = MutableLiveData<Resource<List<OfferResponse>>>()
    val viewAllOfferState: MutableLiveData<Resource<List<OfferResponse>>>
        get() = _viewAllOfferState

    private val _addToFavorites = MutableLiveData<Resource<Boolean>>()
    val addToFavorites: MutableLiveData<Resource<Boolean>>
        get() = _addToFavorites
 private val _getFavorites = MutableLiveData<Resource<List<FavouritesDto>>>()
     val getFavorites: MutableLiveData<Resource<List<FavouritesDto>>>
        get() = _getFavorites

    private val _deleteFavorite = MutableLiveData<Resource<Boolean>>()
     val deleteFavorite: MutableLiveData<Resource<Boolean>>
        get() = _deleteFavorite

    var favourite = listOf<FavouritesDto>()

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
            when (it) {
                is Resource.Success -> {
                    _viewAllOfferState.value = Resource.Success(it.data!!)
                }
                is Resource.Error -> _viewAllOfferState.value = Resource.Error("NieudanoVM")
                is Resource.Loading -> _viewAllOfferState.value = Resource.Loading()
            }
        }.launchIn(viewModelScope)
    }

    fun addOfferToFavorites(offerId: Int) {
        viewModelScope.launch {
            try{
                addToFavorites.postValue(Resource.Loading())
                addToFavoritesUseCase(offerId)
                addToFavorites.postValue(Resource.Success())
            } catch (e: HttpException) {
                addToFavorites.postValue(Resource.Error(e.localizedMessage))
            } catch(e: IOException) {
                addToFavorites.postValue(Resource.Error(e.localizedMessage ))
            }
        }
    }

    fun getFavoriteOffers() {
        viewModelScope.launch {
            try{
                getFavorites.postValue(Resource.Loading())
                val nickname = sharedPreferencesService.getLoggedUsername()
                val favorites = getFavoriteOffersUseCase(nickname)
                val idList = mutableListOf<Int>()
                favourite = favorites
                println(favorites + "FAV!!")
                getFavorites.postValue(Resource.Success(favorites))

            } catch (e: HttpException) {
                getFavorites.postValue(Resource.Error(e.localizedMessage))
            } catch(e: IOException) {
                getFavorites.postValue(Resource.Error(e.localizedMessage ))
            }
        }
    }

    fun deleteOfferFromFavorites(offer: OfferResponse) {
        viewModelScope.launch {
            try {
                val username = sharedPreferencesService.getLoggedUsername()
                deleteFavorite.postValue(Resource.Loading())
                val favoriteId = favourite.filter {it.id == offer.id}
                println(favourite)
                println(favoriteId)
               deleteOfferFromFavoritesUseCase(favoriteId[0].favouritesId, username)
                deleteFavorite.postValue(Resource.Success())

            } catch (e: HttpException) {
                deleteFavorite.postValue(Resource.Error(e.localizedMessage))
            } catch(e: IOException) {
                deleteFavorite.postValue(Resource.Error(e.localizedMessage))
            }
        }
    }
}