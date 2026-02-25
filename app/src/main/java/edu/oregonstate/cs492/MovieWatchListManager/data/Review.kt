package edu.oregonstate.cs492.MovieWatchListManager.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Review(
    //Used to navigate to the website on the phone
    var url: String
)
