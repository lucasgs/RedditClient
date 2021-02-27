package com.dendron.redditclient.domain

sealed class ResultWrapper<out T> {
    object Success : ResultWrapper<Nothing>()
    data class Error(val message: String) : ResultWrapper<Nothing>()
}
