package com.example.players.data.repository

import com.example.players.data.mapper.toDomain
import com.example.players.data.remote.ProductsApi
import com.example.players.domain.error.AppError
import com.example.players.domain.model.Product
import com.example.players.domain.model.ProductsPage
import com.example.players.domain.repository.ProductsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProductsRepositoryImpl(
    private val api: ProductsApi,
    private val ioDispatcher: CoroutineDispatcher
) : ProductsRepository {

    override suspend fun getProducts(query: String, limit: Int, skip: Int): Result<ProductsPage> =
        safeCall {
            val response = if (query.isBlank()) {
                api.getProducts(limit, skip)
            } else {
                api.searchProducts(query, limit, skip)
            }
            response.toDomain()
        }

    override suspend fun getProduct(id: Int): Result<Product> = safeCall {
        api.getProduct(id).toDomain() ?: throw AppError.Unknown()
    }

    /** Выполняет запрос на IO-диспатчере и переводит исключения в доменные ошибки. */
    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
        withContext(ioDispatcher) {
            try {
                Result.success(block())
            } catch (e: CancellationException) {
                throw e
            } catch (e: AppError) {
                Result.failure(e)
            } catch (e: IOException) {
                Result.failure(AppError.NoInternet(e))
            } catch (e: HttpException) {
                Result.failure(AppError.Server(e.code()))
            } catch (e: Exception) {
                Result.failure(AppError.Unknown(e))
            }
        }
}
