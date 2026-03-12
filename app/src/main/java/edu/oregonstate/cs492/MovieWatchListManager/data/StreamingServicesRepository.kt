package edu.oregonstate.cs492.MovieWatchListManager.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StreamingServicesRepository(
    private val service: TMDBService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadStreamingServices(movieId: Int): Result<StreamingServices> =
        withContext(ioDispatcher){
            try {
                val res = service.getStreamingServices(movieId)
                if (res.isSuccessful){
                    res.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Empty response body"))
                }else{
                    Result.failure(Exception(res.errorBody()?.string()))
                }
            }catch (e: Exception) {
                Result.failure(e)
            }
        }
}