package com.herpace.data.remote

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: HttpException) {
        val errorBody = try {
            e.response()?.errorBody()?.string()
        } catch (_: Exception) {
            null
        }
        val message = parseErrorMessage(errorBody) ?: e.message()
        ApiResult.Error(e.code(), message)
    } catch (e: IOException) {
        ApiResult.NetworkError
    } catch (e: Exception) {
        ApiResult.Error(-1, e.message)
    }
}

private fun parseErrorMessage(errorBody: String?): String? {
    if (errorBody.isNullOrBlank()) return null
    return try {
        val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
        val element = json.parseToJsonElement(errorBody)
        val obj = element as? kotlinx.serialization.json.JsonObject ?: return errorBody
        (obj["message"] as? kotlinx.serialization.json.JsonPrimitive)?.content
            ?: (obj["title"] as? kotlinx.serialization.json.JsonPrimitive)?.content
            ?: errorBody
    } catch (_: Exception) {
        errorBody
    }
}
