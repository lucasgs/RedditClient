package com.dendron.redditclient.remote

import com.dendron.redditclient.remote.model.PostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

object Api {
    const val Top = "/r/all/top.json"
}

interface RedditApi {

    @GET(Api.Top)
    suspend fun fetchMorePost(
        @Query("limit") limit: Int,
        @Query("after") after: String
    ): Response<PostResponse>
}