package com.example.antly.data.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("accessToken")
    val token: String
)

fun LoginResponseDto.getToken(): String {
    return token
}