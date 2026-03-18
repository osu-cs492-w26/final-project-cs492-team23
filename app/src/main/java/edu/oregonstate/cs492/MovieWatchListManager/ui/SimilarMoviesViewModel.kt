package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie
import edu.oregonstate.cs492.MovieWatchListManager.data.SearchForMovieRepository
import edu.oregonstate.cs492.MovieWatchListManager.data.SimilarMoviesRepository
import edu.oregonstate.cs492.MovieWatchListManager.data.TMDBService
import edu.oregonstate.cs492.MovieWatchListManager.util.LoadingStatus
import kotlinx.coroutines.launch

class SimilarMoviesViewModel: ViewModel() {
    private val repository = SimilarMoviesRepository(TMDBService.Companion.create())

    private val _searchResults = MutableLiveData<List<Movie>?>(null)
    val searchResults: LiveData<List<Movie>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadSearchResults(movieId: Int){
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadSearchResults(movieId)
            _searchResults.value = result.getOrNull()
            _errorMessage.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
            if (!result.isSuccess) {
                result.exceptionOrNull()?.message?.let { Log.d("SIMILAR", it) }
            }
        }
    }
}