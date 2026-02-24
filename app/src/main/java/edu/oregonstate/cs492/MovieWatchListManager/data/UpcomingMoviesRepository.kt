package edu.oregonstate.cs492.MovieWatchListManager.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpcomingMoviesRepository(
    private val service: TMDBService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadUpcomingMovies(): Result<List<Movie>> =
        withContext(ioDispatcher){
            try {
                val res = service.getUpcomingMovies()
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