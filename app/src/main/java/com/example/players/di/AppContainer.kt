package com.example.players.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.players.data.remote.ProductsApi
import com.example.players.data.repository.ProductsRepositoryImpl
import com.example.players.domain.repository.ProductsRepository
import com.example.players.domain.usecase.GetProductDetailsUseCase
import com.example.players.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** Простой ручной DI-контейнер (без Hilt/Koin). */
class AppContainer(context: Context) {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor.Builder(context).build())
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val api: ProductsApi = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductsApi::class.java)

    private val repository: ProductsRepository = ProductsRepositoryImpl(api, Dispatchers.IO)

    val getProductsUseCase = GetProductsUseCase(repository)
    val getProductDetailsUseCase = GetProductDetailsUseCase(repository)
}
