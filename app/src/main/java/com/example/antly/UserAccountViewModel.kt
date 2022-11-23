package com.example.antly

import androidx.lifecycle.ViewModel
import com.example.antly.sharedPreferences.SharedPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(val sharedPreferencesService: SharedPreferencesService): ViewModel() {

    fun logout() {
        sharedPreferencesService.saveApiToken("")
    }

    fun getLoggedUserName(): String {
        return sharedPreferencesService.getLoggedUsername()
    }

}