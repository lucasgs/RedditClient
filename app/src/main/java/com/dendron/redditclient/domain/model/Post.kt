package com.dendron.redditclient.domain.model

data class Post(
    val id: String,
    val title: String,
    val author: String,
    val thumbnail: String?,
    val comments: Int,
    val created: Long,
)
