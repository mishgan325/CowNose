package ru.mishgan325.cownose.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException


val TAG = "RequestHandler"

sealed class RequestError {
    data object NetworkError : RequestError()
    data class ApiError(val message: String?, val code: Int? = null) : RequestError()
    data class UnknownError(val message: String?) : RequestError()
}

sealed class RequestResult<out T> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Failure(val error: RequestError) : RequestResult<Nothing>()

    fun <R> transform(block: (T) -> R): RequestResult<R> = when (this) {
        is Success -> Success(block(this.data))
        is Failure -> Failure(this.error)
    }

}

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): RequestResult<T> {
    return try {
        val response = request { block() }
        RequestResult.Success(response.body())
    } catch (e: ClientRequestException) {
        RequestResult.Failure(RequestError.ApiError(e.message, e.response.status.value))
    } catch (e: ServerResponseException) {
        RequestResult.Failure(RequestError.ApiError(e.message, e.response.status.value))
    } catch (_: IOException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (_: SerializationException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (_: UnresolvedAddressException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (e: Exception) {
        if (e is CancellationException) throw (e)
        Log.d(TAG, "ktor got unknown exception: ${e.message}")
        RequestResult.Failure(RequestError.UnknownError(e.message))

    }
}

suspend inline fun <reified T> HttpClient.safeRequestRaw(
    block: () -> HttpResponse,
): RequestResult<T> {
    return try {
        val response = block()
        RequestResult.Success(response.body())
    } catch (e: ClientRequestException) {
        RequestResult.Failure(RequestError.ApiError(e.message, e.response.status.value))
    } catch (e: ServerResponseException) {
        RequestResult.Failure(RequestError.ApiError(e.message, e.response.status.value))
    } catch (_: IOException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (_: SerializationException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (_: UnresolvedAddressException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (e: Exception) {
        if (e is CancellationException) throw (e)
        Log.d(TAG, "ktor got unknown exception: ${e.message}")

        RequestResult.Failure(RequestError.UnknownError(e.message))

    }
}

suspend inline fun <reified T> HttpClient.safeGet(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): RequestResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Get
    block()
}

suspend inline fun <reified T> HttpClient.safePost(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): RequestResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Post
    block()
}


suspend inline fun <reified T> HttpClient.safePatch(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): RequestResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Patch
    block()
}

suspend inline fun <reified T> HttpClient.safeDelete(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): RequestResult<T> = safeRequest {
    url(urlString)
    method = HttpMethod.Companion.Delete
    block()
}