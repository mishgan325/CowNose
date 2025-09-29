package ru.mishgan325.cownose.data.network.request

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
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> HttpClient.safeRequest(block: HttpRequestBuilder.() -> Unit): RequestResult<T> =
    try {
        val response = request { block() }
        RequestResult.Success(data = response.body())
    } catch (e: ClientRequestException) {
        RequestResult.Failure(
            error = RequestError.ApiError(
                message = e.message,
                code = e.response.status.value
            )
        )
    } catch (e: ServerResponseException) {
        RequestResult.Failure(
            error = RequestError.ApiError(
                message = e.message,
                code = e.response.status.value
            )
        )
    } catch (_: IOException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (_: SerializationException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (_: UnresolvedAddressException) {
        RequestResult.Failure(RequestError.NetworkError)
    } catch (e: Exception) {
        if (e is CancellationException) throw (e)
        Log.d("RequestHandler", "ktor got unknown exception: ${e.message}")
        RequestResult.Failure(error = RequestError.UnknownError(message = e.message))
    }

suspend inline fun <reified T> HttpClient.safeRequestRaw(block: () -> HttpResponse): RequestResult<T> =
    try {
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
        Log.d("RequestHandler", "ktor got unknown exception: ${e.message}")
        RequestResult.Failure(error = RequestError.UnknownError(message = e.message))
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