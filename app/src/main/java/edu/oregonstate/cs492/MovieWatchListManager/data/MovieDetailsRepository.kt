package edu.oregonstate.cs492.MovieWatchListManager.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDetailsRepository(
    private val service: TMDBService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadMovieDetails(movieId: Int): Result<MovieDetails> =
        withContext(ioDispatcher){
            try {
                val res = service.getMovieDetails(movieId)
                if(res.isSuccessful){
                    res.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Empty response body"))
                }else{
                    Result.failure(Exception(res.errorBody()?.string()))
                }
            } catch (e: Exception){
                Result.failure(e)
            }
        }
}