package com.example.quiz.data.util

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    sealed interface Error : ApiResult<Nothing> {
        data class Network(val exception: Throwable) : Error
        data class Http(val code: Int, val message: String, val exception: Throwable? = null) : Error
        data class Unknown(val exception: Throwable) : Error
    }
}
