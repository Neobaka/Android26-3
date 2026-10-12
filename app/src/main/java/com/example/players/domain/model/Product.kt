package com.example.players.domain.model

data class Review(
    val rating: Int,
    val comment: String,
    val reviewerName: String,
    val date: String
)

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val brand: String?,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String>,
    val availability: String,
    val shipping: String,
    val warranty: String,
    val returnPolicy: String,
    val thumbnail: String,
    val images: List<String>,
    val reviews: List<Review>
)

data class ProductsPage(
    val items: List<Product>,
    val total: Int
)
