package com.dendron.redditclient.domain

import com.dendron.redditclient.domain.model.Post

sealed class ApiResult {
    data class Success(val after: String?, val data: List<Post>) : ApiResult()
    data class Error(val message: String) : ApiResult()
}
