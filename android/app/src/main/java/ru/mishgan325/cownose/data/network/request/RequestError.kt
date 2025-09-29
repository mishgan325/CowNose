package ru.mishgan325.cownose.data.network.request

sealed class RequestError {
    data object NetworkError : RequestError()
    data class ApiError(val message: String?, val code: Int? = null) : RequestError()
    data class UnknownError(val message: String?) : RequestError()
}