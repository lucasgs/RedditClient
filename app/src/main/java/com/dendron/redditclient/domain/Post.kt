package com.dendron.redditclient.domain

class Post(
    private val id: String,
    private val title: String,
    private val author: String,
    private val thumbnail: String?,
    private val comments: Int,
    private val created: Float,
)
