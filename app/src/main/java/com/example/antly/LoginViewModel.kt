package com.example.antly

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.dto.UserDto
import com.example.antly.domain.use_case.login_use_case.LoginUseCase
import com.example.antly.sharedPreferences.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val sharedPreferencesService: SharedPreferencesService
    ): ViewModel() {

    private val _viewState = MutableLiveData<Resource<String>>()
    val viewState: LiveData<Resource<String>>
        get() = _viewState

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(nickname: String, password: String) {
        val userDto = UserDto(nickname,password)

        loginUseCase(userDto).onEach {
            when(it) {
                is Resource.Success -> {
                    val decodedApiKey = decodeToken(it.data!!)
                    val username = JSONObject(decodedApiKey).getString("username")
                    println(username)
                    sharedPreferencesService.saveLoggedUsername(username)
                    sharedPreferencesService.saveLoggedUserPassword(userDto.password)
                    sharedPreferencesService.saveApiToken(it.data)
                    println(sharedPreferencesService.getLoggedUsername())

                    _viewState.value = Resource.Success(it.data)
                }
                is Resource.Loading -> _viewState.value = Resource.Loading()
                is Resource.Error ->  _viewState.value = Resource.Error(it.message!!)
            }
        }.launchIn(viewModelScope)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decodeToken(jwt: String): String {
        val parts = jwt.split(".")
        return try {
            val charset = charset("UTF-8")
            val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
            val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
            "$header"
            "$payload"
        } catch (e: Exception) {
            "Error parsing JWT: $e"
        }
    }

}