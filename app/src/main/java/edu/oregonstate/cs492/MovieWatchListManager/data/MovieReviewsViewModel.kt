package edu.oregonstate.cs492.MovieWatchListManager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovieReviewsViewModel: ViewModel() {
    private val repository = MovieReviewsRepository(TMDBService.create())

    private val _searchResults = MutableLiveData<List<Review>?>(null)
    val searchResults: LiveData<List<Review>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    suspend fun loadMovieReviews(movieId: Int){
        _loadingStatus.value = LoadingStatus.LOADING
        val result = repository.loadMovieVideos(movieId)
        _searchResults.value = result.getOrNull()
        _errorMessage.value = result.exceptionOrNull()?.message
        _loadingStatus.value = when(result.isSuccess){
            true-> LoadingStatus.SUCCESS
            false-> LoadingStatus.ERROR
        }
    }
}