package edu.oregonstate.cs492.MovieWatchListManager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NowPlayingMoviesViewModel: ViewModel() {
    private val repository = NowPlayingMoviesRepository(TMDBService.create())

    private val _searchResults = MutableLiveData<List<Movie>?>(null)
    val searchResults: LiveData<List<Movie>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    suspend fun loadNowPlayingMovies(){
        _loadingStatus.value = LoadingStatus.LOADING
        val result = repository.loadNowPlayingMovies()
        _searchResults.value = result.getOrNull()
        _errorMessage.value = result.exceptionOrNull()?.message
        _loadingStatus.value = when(result.isSuccess){
            true-> LoadingStatus.SUCCESS
            false-> LoadingStatus.ERROR
        }
    }
}