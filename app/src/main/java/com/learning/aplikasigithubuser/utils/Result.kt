package com.learning.aplikasigithubuser.utils

sealed class Result {
    data class Success<out T>(val data: T) : com.learning.aplikasigithubuser.utils.Result()
    data class Error(val exception: Throwable): com.learning.aplikasigithubuser.utils.Result()
    data class Loading(val isLoading: Boolean): com.learning.aplikasigithubuser.utils.Result()
}
