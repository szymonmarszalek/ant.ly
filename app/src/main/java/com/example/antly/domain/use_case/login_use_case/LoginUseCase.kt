package com.example.antly.domain.use_case.login_use_case

import com.example.antly.common.Resource
import com.example.antly.data.dto.LoginResponse
import com.example.antly.data.dto.UserDto
import com.example.antly.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    operator fun invoke(userDto: UserDto): Flow<Resource<LoginResponse>> = flow {
        try {
            emit(Resource.Loading<LoginResponse>())
            val authToken = loginRepository.login(userDto)
            emit(Resource.Success<LoginResponse>(authToken))
        } catch (e: HttpException) {
            emit(Resource.Error<LoginResponse>("No connection"))
        } catch (e: IOException) {
            emit(Resource.Error<LoginResponse>("error"))
        }
    }
}