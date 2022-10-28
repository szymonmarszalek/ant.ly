package com.example.antly.sharedPreferences.data

import com.example.antly.sharedPreferences.SharedPreference

class ApiTokenSharedPreference() : SharedPreference() {
    override fun getName(): String {
        return "API_TOKEN"
    }
}