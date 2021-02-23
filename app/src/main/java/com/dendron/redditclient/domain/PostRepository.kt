package com.dendron.redditclient.domain

import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.remote.ResultWrapper

interface PostRepository {
    suspend fun getPosts(): ResultWrapper<List<Post>>
}