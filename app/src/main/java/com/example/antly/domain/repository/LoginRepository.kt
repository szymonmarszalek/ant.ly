package com.example.antly.domain.repository

import com.example.antly.data.RegisterData
import com.example.antly.data.dto.LoginResponse
import com.example.antly.data.dto.UserDto

interface LoginRepository {
    suspend fun login(userDto: UserDto): LoginResponse

    suspend fun register(user: RegisterData): String
}