package com.dendron.redditclient.utils

import com.dendron.redditclient.domain.model.Post

const val POST_LIMIT = 2

fun mockPostList() = listOf(
    Post(
        id = "1",
        title = "title1",
        author = "author1",
        thumbnail = "thumb1",
        comments = 1,
        created = 1,
        image = "image1"
    ),
    Post(
        id = "2",
        title = "title2",
        author = "author2",
        thumbnail = "thumb2",
        comments = 2,
        created = 2,
        image = "image2"
    ),
    Post(
        id = "3",
        title = "title3",
        author = "author3",
        thumbnail = "thumb3",
        comments = 3,
        created = 3,
        image = "image3"
    ),
)
