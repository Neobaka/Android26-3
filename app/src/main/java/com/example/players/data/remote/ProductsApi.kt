package com.example.players.data.remote

import com.example.players.data.remote.dto.ProductDto
import com.example.players.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {

    /** Список с пагинацией: limit / skip (query-параметры). */
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponseDto

    /** Поиск по названию: q (query-параметр). */
    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponseDto

    /** Детали: id (path-параметр). */
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductDto
}
