package com.example.antly.data.dto

import com.google.gson.annotations.SerializedName
import kotlin.String

data class UserDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)
