package com.dendron.redditclient.remote

import com.dendron.redditclient.remote.model.PostResponse
import retrofit2.Response
import retrofit2.http.GET

interface RedditApi {

    @GET("/r/all/top.json")
    suspend fun getPost(): Response<PostResponse>
}