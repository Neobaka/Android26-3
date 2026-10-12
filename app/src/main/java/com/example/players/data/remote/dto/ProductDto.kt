package com.example.players.data.remote.dto

// Все поля nullable: Gson не применяет значения по умолчанию из Kotlin,
// поэтому безопасные значения подставляются в маппере.

data class ProductsResponseDto(
    val products: List<ProductDto>?,
    val total: Int?,
    val skip: Int?,
    val limit: Int?
)

data class ProductDto(
    val id: Int?,
    val title: String?,
    val description: String?,
    val category: String?,
    val brand: String?,
    val price: Double?,
    val discountPercentage: Double?,
    val rating: Double?,
    val stock: Int?,
    val tags: List<String>?,
    val availabilityStatus: String?,
    val shippingInformation: String?,
    val warrantyInformation: String?,
    val returnPolicy: String?,
    val thumbnail: String?,
    val images: List<String>?,
    val reviews: List<ReviewDto>?
)

data class ReviewDto(
    val rating: Int?,
    val comment: String?,
    val date: String?,
    val reviewerName: String?
)
