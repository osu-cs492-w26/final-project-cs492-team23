package edu.oregonstate.cs492.MovieWatchListManager.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieReviews(
    var results: List<Review>
)