package com.example.antly.domain.use_case.login_use_case

import com.example.antly.common.Resource
import com.example.antly.data.RegisterData
import com.example.antly.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    operator fun invoke(registerData: RegisterData): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading<String>())
            val register = loginRepository.register(registerData)
            emit(Resource.Success<String>())
        } catch (e: HttpException) {
            println(e.code())
            emit(Resource.Error<String>(e.code().toString()))
        }
        catch (e: IOException) {
            println(e.localizedMessage!!)
            emit(Resource.Error<String>("Something went wrong"))
        }
    }
}