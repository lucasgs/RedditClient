package com.dendron.redditclient.domain

import com.dendron.redditclient.domain.model.Post

interface PostRepository {
    suspend fun getPosts(limit: Int): ResultWrapper<List<Post>>
    suspend fun dismissPost(post: Post)
    suspend fun dismissAll()
}