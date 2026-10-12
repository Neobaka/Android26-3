package com.example.players.domain.usecase

import com.example.players.domain.model.Product
import com.example.players.domain.repository.ProductsRepository

class GetProductDetailsUseCase(private val repository: ProductsRepository) {
    suspend operator fun invoke(id: Int): Result<Product> = repository.getProduct(id)
}
