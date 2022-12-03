package com.example.antlyimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.example.antly.common.Resourceimport com.example.antly.data.dto.FavouritesDtoimport com.example.antly.data.dto.OfferResponseimport com.example.antly.domain.use_case.offer_use_case.AddToFavoritesUseCaseimport com.example.antly.domain.use_case.offer_use_case.DeleteOfferFromFavoritesUseCaseimport com.example.antly.domain.use_case.offer_use_case.DeleteOfferUseCaseimport com.example.antly.domain.use_case.offer_use_case.GetFavoriteOffersUseCaseimport com.example.antly.sharedPreferences.SharedPreferencesServiceimport dagger.hilt.android.lifecycle.HiltViewModelimport kotlinx.coroutines.launchimport retrofit2.HttpExceptionimport java.io.IOExceptionimport javax.inject.Inject@HiltViewModelclass UserDetailsViewModel @Inject constructor(    val deleteOfferUseCase: DeleteOfferUseCase,    val sharedPreferencesService: SharedPreferencesService,    val addToFavoritesUseCase: AddToFavoritesUseCase,    val getFavoriteOffersUseCase: GetFavoriteOffersUseCase,    val deleteOfferFromFavoritesUseCase: DeleteOfferFromFavoritesUseCase): ViewModel() {    private val _deleteOfferState = MutableLiveData<Resource<List<OfferResponse>>>()    val deleteOfferState: MutableLiveData<Resource<List<OfferResponse>>>        get() = _deleteOfferState    private val _addToFavorites = MutableLiveData<Resource<Boolean>>()    val addToFavorites: MutableLiveData<Resource<Boolean>>        get() = _addToFavorites    private val _getFavorites = MutableLiveData<Resource<List<FavouritesDto>>>()    val getFavorites: MutableLiveData<Resource<List<FavouritesDto>>>        get() = _getFavorites    private val _deleteFavorite = MutableLiveData<Resource<Boolean>>()    val deleteFavorite: MutableLiveData<Resource<Boolean>>        get() = _deleteFavorite    var favourite = listOf<FavouritesDto>()    fun deleteOffer(offerId: Int) {        viewModelScope.launch {            try {                _deleteOfferState.postValue(Resource.Loading())                deleteOfferUseCase(offerId)                _deleteOfferState.postValue(Resource.Success())            } catch (e: HttpException) {                _deleteOfferState.postValue(Resource.Error(e.localizedMessage!!))            } catch (e: IOException) {                _deleteOfferState.postValue(Resource.Error(e.localizedMessage!!))            }        }    }    fun addOfferToFavorites(offerId: Int) {        viewModelScope.launch {            try{                addToFavorites.postValue(Resource.Loading())                addToFavoritesUseCase(offerId)                addToFavorites.postValue(Resource.Success())            } catch (e: HttpException) {                addToFavorites.postValue(Resource.Error(e.localizedMessage))            } catch(e: IOException) {                addToFavorites.postValue(Resource.Error(e.localizedMessage ))            }        }    }    fun getFavoriteOffers() {        viewModelScope.launch {            try{                getFavorites.postValue(Resource.Loading())                val nickname = sharedPreferencesService.getLoggedUsername()                val favorites = getFavoriteOffersUseCase(nickname)                favourite = favorites                getFavorites.postValue(Resource.Success(favorites))            } catch (e: HttpException) {                getFavorites.postValue(Resource.Error(e.localizedMessage))            } catch(e: IOException) {                getFavorites.postValue(Resource.Error(e.localizedMessage ))            }        }    }    fun deleteOfferFromFavorites(offer: OfferResponse) {        viewModelScope.launch {            try {                val username = sharedPreferencesService.getLoggedUsername()                deleteFavorite.postValue(Resource.Loading())                val favoriteId = favourite.filter {it.id == offer.id}                deleteOfferFromFavoritesUseCase(favoriteId[0].favouritesId, username)                deleteFavorite.postValue(Resource.Success())            } catch (e: HttpException) {                deleteFavorite.postValue(Resource.Error(e.localizedMessage))            } catch(e: IOException) {                deleteFavorite.postValue(Resource.Error(e.localizedMessage))            }        }    }}