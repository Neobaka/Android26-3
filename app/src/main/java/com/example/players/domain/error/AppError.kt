package com.example.players.domain.error

/** Доменные ошибки: слой представления ничего не знает про Retrofit/OkHttp. */
sealed class AppError(cause: Throwable? = null) : Exception(cause) {
    class NoInternet(cause: Throwable? = null) : AppError(cause)
    class Server(val code: Int) : AppError()
    class Unknown(cause: Throwable? = null) : AppError(cause)
}
