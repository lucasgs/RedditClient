package com.dendron.redditclient.remote

import com.dendron.redditclient.remote.model.PostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    @GET("/r/all/top.json")
    suspend fun getPost(@Query("limit") limit: Int): Response<PostResponse>
}