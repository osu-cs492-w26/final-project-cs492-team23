package edu.oregonstate.cs492.MovieWatchListManager.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResults (
    val results: List<Movie>
)