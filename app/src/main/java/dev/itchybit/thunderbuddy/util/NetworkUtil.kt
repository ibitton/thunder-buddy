package dev.itchybit.thunderbuddy.util

import dev.itchybit.thunderbuddy.io.api.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.SerializationException
import java.io.IOException

object NetworkUtil {

    suspend inline fun <reified T> HttpClient.safeCommonRequest(
        block: HttpRequestBuilder.() -> Unit,
    ): ApiResponse<T, String> = safeRequest<T, String>(block)

    suspend inline fun <reified T, reified E> HttpClient.safeRequest(
        block: HttpRequestBuilder.() -> Unit,
    ): ApiResponse<T, E> = try {
        val response = request { block() }
        ApiResponse.Success(response.body())
    } catch (e: ClientRequestException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: ServerResponseException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: IOException) {
        ApiResponse.Error.NetworkError
    } catch (e: SerializationException) {
        ApiResponse.Error.SerializationError
    } catch (e: JsonConvertException) {
        ApiResponse.Error.SerializationError
    }

    suspend inline fun <reified E> ResponseException.errorBody(): E? = try {
        response.body()
    } catch (e: SerializationException) {
        null
    }
}