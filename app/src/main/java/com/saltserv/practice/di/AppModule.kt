package com.saltserv.practice.di

import com.saltserv.practice.data.api.YourApi
import com.saltserv.practice.data.repository.YourRepository
import com.saltserv.practice.data.repository.YourRepositoryImpl
import com.saltserv.practice.domain.YourGetDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesHttpClient() = OkHttpClient().newBuilder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Provides
    fun providesYourApi(): YourApi = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(providesHttpClient())
        .build()
        .create(YourApi::class.java)

    @Provides
    fun providesYourRepository(
        yourApi: YourApi
    ) = YourRepositoryImpl(yourApi)

    @Provides
    fun providesYourGetDataUseCase(
        yourRepository: YourRepository
    ) = YourGetDataUseCase(yourRepository)
}