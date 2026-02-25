package edu.oregonstate.cs492.MovieWatchListManager.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetails(
    //Runtime is in # of minutes
    var runtime: Int,
)
