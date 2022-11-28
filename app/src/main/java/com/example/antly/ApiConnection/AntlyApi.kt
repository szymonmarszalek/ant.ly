package com.example.antly.ApiConnection

import com.example.antly.data.RegisterData
import com.example.antly.data.dto.LoginResponseDto
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.data.dto.UserDto
import retrofit2.http.*

interface AntlyApi {
    @POST("auth/signin")
    suspend fun loginUser(
        @Body user: UserDto,
    ): LoginResponseDto

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

    @DELETE("/offers/{id}")
    suspend fun deleteOffer(
        @Path("id") offerId: Int,
    )

    @PATCH("/offers/{id}")
    suspend fun editOffer(
        @Path("id") offerId: Int,
        @Body offer: Offer
    )
}