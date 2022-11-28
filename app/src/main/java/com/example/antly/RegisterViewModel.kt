package com.example.antly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antly.common.Resource
import com.example.antly.data.RegisterData
import com.example.antly.domain.use_case.login_use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _viewState = MutableLiveData<Resource<String>>()
    val viewState: LiveData<Resource<String>>
        get() = _viewState


    fun register(registerData: RegisterData) {
        registerUseCase(registerData).onEach {
            when (it) {
                is Resource.Success -> _viewState.value = Resource.Success()
                is Resource.Error -> _viewState.value = Resource.Error(it.message!!)
                is Resource.Loading -> _viewState.value = Resource.Loading()
            }
        }.launchIn(viewModelScope)

    }
}