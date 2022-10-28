package com.example.antly.di

import com.example.antly.RetrofitBuilder
import org.koin.dsl.module

@JvmField
val applicationModule = module {
    //Builders
    single { RetrofitBuilder() }
}
