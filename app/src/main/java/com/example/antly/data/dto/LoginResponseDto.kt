package com.example.antly.data.dto

import com.google.gson.annotations.SerializedName
import kotlin.String

data class LoginResponseDto(
    @SerializedName("accessToken")
    val token: String
)

fun LoginResponseDto.getToken(): String {
    return token
}