package com.dendron.redditclient.domain

import com.dendron.redditclient.remote.ResultWrapper

interface PostRepository {
    suspend fun getPosts(limit: Int): ResultWrapper
}