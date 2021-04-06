package com.example.grilledcheese.utils

data class Resource<out T>(
    val status: Status, val data: T?, val message: String? = null
) {

    companion object {

        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data)

        fun <T> loading(data: T? = null): Resource<T> = Resource(Status.LOADING, data)

        fun <T> error(data: T? = null, message: String?): Resource<T> =
            Resource(Status.ERROR, data, message ?: "")
    }
}

enum class Status { SUCCESS, LOADING, ERROR }