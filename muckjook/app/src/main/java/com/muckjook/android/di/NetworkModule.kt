package com.muckjook.android.di

import com.muckjook.android.widget.utils.Utils.BASE_URL
import com.muckjook.data.remote.api.StoreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
        ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .client(provideHttpClient())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideStoreApiService(retrofit: Retrofit): StoreApi {
        return retrofit.create(StoreApi::class.java)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}