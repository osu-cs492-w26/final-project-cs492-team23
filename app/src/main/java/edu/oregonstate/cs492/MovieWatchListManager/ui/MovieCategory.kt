package edu.oregonstate.cs492.MovieWatchListManager.ui

import edu.oregonstate.cs492.MovieWatchListManager.data.Movie

data class MovieCategory(
    val title: String,
    val movies: List<Movie>
)