package edu.oregonstate.cs492.MovieWatchListManager.data

import java.io.Serializable

data class MovieCategory(
    val title: String,
    val movies: List<Movie>
) : Serializable
