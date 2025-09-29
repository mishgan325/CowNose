package ru.mishgan325.cownose.data.network.request

sealed class RequestResult<out T> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Failure(val error: RequestError) : RequestResult<Nothing>()

    fun <R> transform(block: (T) -> R): RequestResult<R> = when (this) {
        is Success -> Success(data = block(this.data))
        is Failure -> Failure(error = this.error)
    }
}