package com.dendron.redditclient.util

import android.widget.ImageView
import com.dendron.redditclient.R
import com.squareup.picasso.Picasso

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

fun ImageView.loadImage(url: String?) {
    Picasso
        .get()
        .load(url)
        //.centerCrop()
        .error(R.drawable.ic_no_image)
        .placeholder(R.drawable.ic_no_image)
        .into(this)
}

fun Long.toRelativeTime(): String {
    val now = System.currentTimeMillis()
    var time = this
    // if timestamp given in seconds, convert to millis
    if (time < 1000000000000L) {
        time *= 1000
    }

    val diff = now - time
    return when {
        diff < MINUTE_MILLIS -> "moments ago"
        diff < 2 * MINUTE_MILLIS -> "a minute ago"
        diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
        diff < 2 * HOUR_MILLIS -> "an hour ago"
        diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
        diff < 48 * HOUR_MILLIS -> "yesterday"
        else -> "${diff / DAY_MILLIS} days ago"
    }
}

