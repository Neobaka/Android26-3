package com.example.players.data.mapper

import com.example.players.data.remote.dto.ProductDto
import com.example.players.data.remote.dto.ProductsResponseDto
import com.example.players.data.remote.dto.ReviewDto
import com.example.players.domain.model.Product
import com.example.players.domain.model.ProductsPage
import com.example.players.domain.model.Review

fun ReviewDto.toDomain() = Review(
    rating = rating ?: 0,
    comment = comment.orEmpty(),
    reviewerName = reviewerName.orEmpty(),
    date = date.orEmpty().take(10)
)

fun ProductDto.toDomain(): Product? {
    val productId = id ?: return null
    return Product(
        id = productId,
        title = title.orEmpty(),
        description = description.orEmpty(),
        category = category.orEmpty(),
        brand = brand,
        price = price ?: 0.0,
        discountPercentage = discountPercentage ?: 0.0,
        rating = rating ?: 0.0,
        stock = stock ?: 0,
        tags = tags.orEmpty(),
        availability = availabilityStatus.orEmpty(),
        shipping = shippingInformation.orEmpty(),
        warranty = warrantyInformation.orEmpty(),
        returnPolicy = returnPolicy.orEmpty(),
        thumbnail = thumbnail.orEmpty(),
        images = images.orEmpty(),
        reviews = reviews.orEmpty().map { it.toDomain() }
    )
}

fun ProductsResponseDto.toDomain(): ProductsPage {
    val items = products.orEmpty().mapNotNull { it.toDomain() }
    return ProductsPage(items = items, total = total ?: items.size)
}
