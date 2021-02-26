package com.dendron.redditclient.domain.model

data class Post(
    val id: String,
    val title: String,
    val author: String,
    val thumbnail: String?,
    val image: String?,
    val comments: Int,
    val created: Long,
    var status: Status = Status.unread
)


enum class Status { unread, read}
