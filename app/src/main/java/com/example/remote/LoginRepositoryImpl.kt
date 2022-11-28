package com.example.remote

import com.example.antly.ApiConnection.AntlyApi
import com.example.antly.data.RegisterData
import com.example.antly.data.dto.LoginResponseDto
import com.example.antly.data.dto.UserDto
import com.example.antly.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(val api: AntlyApi): LoginRepository {
    override suspend fun login(userDto: UserDto): LoginResponseDto {
        return api.loginUser(userDto)
    }

    override suspend fun register(user: RegisterData){
        return  api.registerUser(user)
    }

}