package edu.oregonstate.cs492.MovieWatchListManager.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SimilarMoviesRepository(
    private val service: TMDBService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadSearchResults(movieId: Int): Result<List<Movie>> =
        withContext(ioDispatcher){
            try {
                val res = service.getSimilarMovies(movieId)
                if(res.isSuccessful){
                    Result.success(res.body()?.results ?: listOf())
                }else{
                    Result.failure(Exception(res.errorBody()?.string()))
                }
            } catch (e: Exception){
                Result.failure(e)
            }
        }
}