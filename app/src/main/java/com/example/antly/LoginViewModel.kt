package com.example.antly

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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val sharedPreferencesService: SharedPreferencesService
    ): ViewModel() {

    private val _viewState = MutableLiveData<Resource<String>>()
    val viewState: LiveData<Resource<String>>
        get() = _viewState

    fun login(nickname: String, password: String) {
        val userDto = UserDto(nickname,password)
        println(userDto)
        loginUseCase(userDto).onEach {
            when(it) {
                is Resource.Success -> {
                    sharedPreferencesService.saveApiToken(it.data!!.token)
                }
                is Resource.Error -> println("nieudano")
            }
        }.launchIn(viewModelScope)
    }

}