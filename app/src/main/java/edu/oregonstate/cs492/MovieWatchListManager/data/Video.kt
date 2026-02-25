package edu.oregonstate.cs492.MovieWatchListManager.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Video(
    val type: String,
    //To recover the video: https://www.youtube.com/watch?v=$key
    val key: String
)
