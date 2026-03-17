package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.MovieWatchListManager.data.AppDatabase
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie
import edu.oregonstate.cs492.MovieWatchListManager.data.MovieEntity
import edu.oregonstate.cs492.MovieWatchListManager.data.MovieListRepository
import edu.oregonstate.cs492.MovieWatchListManager.data.toEntity
import kotlinx.coroutines.launch

class MovieListViewModel(application: Application):
    AndroidViewModel(application)
{
    private val repository = MovieListRepository(
        AppDatabase.Companion.getInstance(application).movieDao()
    )

    val movieList = repository.getAllMoviesInList().asLiveData()

    fun addMovieToList(movie: Movie){
        viewModelScope.launch {
            repository.insertMovieIntoList(movie.toEntity())
        }
    }

    fun removeMovieFromList(movie: Movie){
        viewModelScope.launch {
            repository.deleteMovieFromList(movie.toEntity())
        }
    }
}