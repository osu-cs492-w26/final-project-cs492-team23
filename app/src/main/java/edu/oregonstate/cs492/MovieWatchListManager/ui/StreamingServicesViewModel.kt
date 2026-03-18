package edu.oregonstate.cs492.MovieWatchListManager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.MovieWatchListManager.data.StreamingServices
import edu.oregonstate.cs492.MovieWatchListManager.data.StreamingServicesRepository
import edu.oregonstate.cs492.MovieWatchListManager.data.TMDBService
import edu.oregonstate.cs492.MovieWatchListManager.util.LoadingStatus
import kotlinx.coroutines.launch

class StreamingServicesViewModel: ViewModel() {
    private val repository = StreamingServicesRepository(TMDBService.create())

    private val _searchResults = MutableLiveData<StreamingServices>(null)
    val searchResults: LiveData<StreamingServices> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadStreamingServices(movieId: Int) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadStreamingServices(movieId)
            _searchResults.value = result.getOrNull()
            _errorMessage.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}