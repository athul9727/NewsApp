package com.example.newsapp.util

sealed class AppResult<out T> {

    data class Success<out T>(val successData : T) : AppResult<T>()

    class Error(val exception: java.lang.Exception, val message: String = exception.localizedMessage)
        : AppResult<Nothing>()

}
