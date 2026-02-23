package edu.oregonstate.cs492.MovieWatchListManager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PopularMoviesViewModel: ViewModel() {
    private val repository = PopularMoviesRepository(TMDBService.create())

    private val _searchResults = MutableLiveData<List<PopularMovies>?>(null)
    val searchResults: LiveData<List<PopularMovies>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    suspend fun loadPopularMovies(){
        _loadingStatus.value = LoadingStatus.LOADING
        val result = repository.loadPopularMovies()
        _searchResults.value = result.getOrNull()
        _errorMessage.value = result.exceptionOrNull()?.message
        _loadingStatus.value = when(result.isSuccess){
            true-> LoadingStatus.SUCCESS
            false-> LoadingStatus.ERROR
        }
    }
}