package com.example.antly.ApiConnection

import com.example.antly.data.RegisterData
import com.example.antly.data.dto.LoginResponse
import com.example.antly.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AntlyApi {

    @POST("auth/signin")
    suspend fun loginUser(
        @Body user: UserDto
    ): LoginResponse

    @POST("auth/signup")
    suspend fun registerUser(@Body registerData: RegisterData): String
}