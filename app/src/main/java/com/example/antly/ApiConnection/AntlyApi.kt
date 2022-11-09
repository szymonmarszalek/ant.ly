package com.example.antly.ApiConnection

import com.example.antly.data.RegisterData
import com.example.antly.data.dto.LoginResponseDto
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AntlyApi {
    @POST("auth/signin")
    suspend fun loginUser(
        @Body user: UserDto
    ): LoginResponseDto

    @POST("auth/signup")
    suspend fun registerUser(@Body registerData: RegisterData): String

    @GET("/offers")
    suspend fun getAllOffers(): List<OfferResponse>

    @POST("/offers")
    suspend fun postOffer(@Body offer: Offer): OfferResponse
}