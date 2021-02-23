package com.dendron.redditclient.data

import com.dendron.redditclient.domain.Post

interface RemoteDataSource {

    suspend fun getPosts(): List<Post>

}