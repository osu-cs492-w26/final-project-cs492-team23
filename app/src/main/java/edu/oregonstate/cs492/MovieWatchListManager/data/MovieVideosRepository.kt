package edu.oregonstate.cs492.MovieWatchListManager.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieVideosRepository(
    private val service: TMDBService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadMovieVideos(movieId: Int): Result<List<Video>> =
        withContext(ioDispatcher){
            try {
                val res = service.getMovieVideos(movieId)
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