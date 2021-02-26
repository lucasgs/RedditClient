package com.dendron.redditclient.remote.model

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("data") val `data`: Data,
    @SerializedName("kind") val kind: String
) {
    data class Data(
        @SerializedName("after") val after: String,
        @SerializedName("before") val before: Any,
        @SerializedName("children") val children: List<Children>,
        @SerializedName("dist") val dist: Int,
        @SerializedName("modhash") val modhash: String
    ) {
        data class Children(
            @SerializedName("data") val `data`: Data,
            @SerializedName("kind") val kind: String
        ) {
            data class Data(
                @SerializedName("id") val id: String,
                @SerializedName("title") val title: String,
                @SerializedName("author") val author: String,
                @SerializedName("thumbnail") val thumbnail: String,
                @SerializedName("num_comments") val numComments: Int,
                @SerializedName("created") val created: Long,
                @SerializedName("url_overridden_by_dest") val urlOverriddenByDest: String,
            )
        }
    }
}