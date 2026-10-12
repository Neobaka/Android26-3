package com.example.players.domain.usecase

import com.example.players.domain.model.ProductsPage
import com.example.players.domain.repository.ProductsRepository

class GetProductsUseCase(private val repository: ProductsRepository) {
    suspend operator fun invoke(query: String, limit: Int, skip: Int): Result<ProductsPage> =
        repository.getProducts(query.trim(), limit, skip)
}
