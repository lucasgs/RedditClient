package com.dendron.redditclient.domain

import com.dendron.redditclient.domain.model.Post

interface PostRepository {
    suspend fun getPosts(limit: Int): ResultWrapper<List<Post>>
}