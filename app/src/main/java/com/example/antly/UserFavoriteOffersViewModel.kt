package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.FavouritesDto
import com.example.antly.data.dto.OfferResponse
import com.example.antly.domain.use_case.offer_use_case.AddToFavoritesUseCase
import com.example.antly.domain.use_case.offer_use_case.DeleteOfferFromFavoritesUseCase
import com.example.antly.domain.use_case.offer_use_case.GetFavoriteOffersUseCase
import com.example.antly.sharedPreferences.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserFavoriteOffersViewModel @Inject constructor(
    val addToFavoritesUseCase: AddToFavoritesUseCase,
    val getFavoriteOffersUseCase: GetFavoriteOffersUseCase,
    val deleteOfferFromFavoritesUseCase: DeleteOfferFromFavoritesUseCase,
    val sharedPreferencesService: SharedPreferencesService
): ViewModel() {
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