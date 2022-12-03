package com.example.antly.ApiConnection

import com.example.antly.data.RegisterData
import com.example.antly.data.dto.*
import retrofit2.http.*

interface AntlyApi {
    @POST("auth/signin")
    suspend fun loginUser(
        @Body user: UserDto,
    ): LoginResponseDto

    @POST("/favourites")
    suspend fun addOfferToFavorites(
        @Body favoriteOffer: FavoriteOffer,
    )

    @POST("auth/signup")
    suspend fun registerUser(@Body registerData: RegisterData)

    @GET("/offers")
    suspend fun getAllOffers(): List<OfferResponse>

    @GET("/offers/{id}")
    suspend fun getOfferById(
        @Path("id") id: Int,
    ): OfferResponse

    @POST("/offers")
    suspend fun postOffer(@Body offer: Offer): OfferResponse

    @GET("/offers")
    suspend fun getFilteredOffers(
        @Query("subject") subject: String,
        @Query("range") range: String,
        @Query("location") location: String,
    ): List<OfferResponse>

    @GET("/offers/teacher/{teachername}")
    suspend fun getAddedOffers(
        @Path("teachername") teacherName: String,
    ): List<OfferResponse>

    @GET("/favourites/{username}")
    suspend fun getFavoriteOffers(
        @Path("username") username: String,
    ): List<FavouritesDto>

    @DELETE("/offers/{id}")
    suspend fun deleteOffer(
        @Path("id") offerId: Int,
    )

    @DELETE("/favourites/{userName}/{offerId}")
    suspend fun deleteFromFavorites(
        @Path("offerId") offerId: Int,
        @Path("userName") username: String
    )

    @PATCH("/offers/{id}")
    suspend fun editOffer(
        @Path("id") offerId: Int,
        @Body offer: Offer
    )
}