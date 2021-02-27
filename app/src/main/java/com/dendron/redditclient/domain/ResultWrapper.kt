package com.dendron.redditclient.domain

sealed class ResultWrapper {
    data class Success(val nextPage: String? = null) : ResultWrapper()
    data class Error(val message: String) : ResultWrapper()
}
