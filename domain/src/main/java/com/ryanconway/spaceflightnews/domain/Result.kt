package com.ryanconway.spaceflightnews.domain

sealed class Result<out T> {

    class Success<out T>(val value: T): Result<T>() {
        companion object {
            operator fun invoke() = Success(Unit)
        }
    }

    class Failure<out T>(val cause: Throwable): Result<T>()

    class Loading<out T>: Result<T>() {
        companion object {
            operator fun invoke() = Loading<Unit>()
        }
    }
}