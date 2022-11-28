package com.example.antly.domain.repository

import com.example.antly.data.RegisterData
import com.example.antly.data.dto.LoginResponseDto
import com.example.antly.data.dto.UserDto

interface LoginRepository {
    suspend fun login(userDto: UserDto): LoginResponseDto

    suspend fun register(user: RegisterData)
}