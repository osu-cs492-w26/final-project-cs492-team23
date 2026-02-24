package edu.oregonstate.cs492.MovieWatchListManager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Movie(
    @PrimaryKey
    @Json(name = "id") val movieId: Int,
    val overview: String,
    //Add to the end of this string: https://image.tmdb.org/t/p/original
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "release_date") val releaseDate: String,
    val title: String,
    //If there is a trailer or video associated with the move
    val video: Boolean
)
