package com.dendron.redditclient.remote

import retrofit2.Response
import retrofit2.http.GET

interface RedditApi {

    @GET("/r/all/top.json")
    suspend fun getPost(): Response<PostResponse>
}