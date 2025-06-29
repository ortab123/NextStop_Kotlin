package com.example.final_nextstop.util

suspend fun <T> safeCall(action: suspend () -> T): Resource<T> {
    return try {
        Resource.success(action())
    } catch (e: Exception) {
        Resource.error(e.message ?: "An unknown error occurred")
    }
}
