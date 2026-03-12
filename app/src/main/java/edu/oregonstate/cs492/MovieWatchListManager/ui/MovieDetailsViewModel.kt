package edu.oregonstate.cs492.MovieWatchListManager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.oregonstate.cs492.MovieWatchListManager.data.MovieDetails
import edu.oregonstate.cs492.MovieWatchListManager.data.MovieDetailsRepository
import edu.oregonstate.cs492.MovieWatchListManager.data.TMDBService
import edu.oregonstate.cs492.MovieWatchListManager.util.LoadingStatus

class MovieDetailsViewModel: ViewModel() {
    private val repository = MovieDetailsRepository(TMDBService.Companion.create())

    private val _searchResults = MutableLiveData<MovieDetails>(null)
    val searchResults: LiveData<MovieDetails> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    suspend fun loadMovieDetails(movieId: Int){
        _loadingStatus.value = LoadingStatus.LOADING
        val result = repository.loadMovieDetails(movieId)
        _searchResults.value = result.getOrNull()
        _errorMessage.value = result.exceptionOrNull()?.message
        _loadingStatus.value = when(result.isSuccess){
            true-> LoadingStatus.SUCCESS
            false-> LoadingStatus.ERROR
        }
    }
}