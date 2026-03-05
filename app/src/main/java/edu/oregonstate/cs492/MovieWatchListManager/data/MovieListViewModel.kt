package edu.oregonstate.cs492.MovieWatchListManager.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieListViewModel(application: Application):
    AndroidViewModel(application)
{
    private val repository = MovieListRepository(
        AppDatabase.getInstance(application).movieDao()
    )

    val movieList = repository.getAllMoviesInList().asLiveData()

    fun addMovieToList(movie: Movie){
        viewModelScope.launch {
            repository.insertMovieIntoList(movie)
        }
    }

    fun removeMovieFromList(movie: Movie){
        viewModelScope.launch {
            repository.deleteMovieFromList(movie)
        }
    }
}