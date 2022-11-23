package com.example.antly.sharedPreferences.data

import com.example.antly.sharedPreferences.SharedPreference

class LoggedUsernameSharedPreference() : SharedPreference() {
    override fun getName(): String {
        return "LOGGED_USERNAME"
    }
}