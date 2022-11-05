package com.example.antly.di

import android.content.Context
import com.example.antly.ApiConnection.AntlyApi
import com.example.antly.ApiConnection.BuildHttpClient
import com.example.antly.common.Constants
import com.example.antly.domain.repository.LoginRepository
import com.example.antly.domain.repository.OffersRepository
import com.example.antly.sharedPreferences.SharedPreferencesService
import com.example.remote.LoginRepositoryImpl
import com.example.remote.OffersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAntlyApi(buildHttpClient: BuildHttpClient): AntlyApi {
        return Retrofit
            .Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient.buildHttpClient())
            .build()
            .create(AntlyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(api: AntlyApi): LoginRepository {
        return LoginRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideOfferRepository(api: AntlyApi): OffersRepository {
        return OffersRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesService(@ApplicationContext appContext: Context): SharedPreferencesService {
        return SharedPreferencesService(appContext)
    }

    @Provides
    @Singleton
    fun provideHttpClient(sharedPreferencesService: SharedPreferencesService): BuildHttpClient {
        return BuildHttpClient(sharedPreferencesService)
    }
}