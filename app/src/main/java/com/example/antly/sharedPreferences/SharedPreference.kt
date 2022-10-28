package com.example.antly.sharedPreferences

abstract class SharedPreference {
    var value: String = ""
    abstract fun getName(): String
}