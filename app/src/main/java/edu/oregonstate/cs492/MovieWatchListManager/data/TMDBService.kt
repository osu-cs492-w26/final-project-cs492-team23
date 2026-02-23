package edu.oregonstate.cs492.MovieWatchListManager.data

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//Put your key here:
val api_key = ""
interface TMDBService {


    //GET /movie/popular
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") key: String =  api_key
    ): Response<PopularMoviesResults>

//    @GET("movie/{movie_id}/videos")
//    suspend fun getMovieVideos(
//        @Path("movie_id") movieID: Int = 1236153,
//        @Query("language") lang: String = "en-US",
//        @Query("api_key") key: String = api_key
//    ): Respond<String>


    companion object {

        private val BASE_URL = "https://api.themoviedb.org/3/"

        // GitHubService.create()
        fun create(): TMDBService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //TODO use moshi factor, and change gradle, toml dependencies
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(TMDBService::class.java)
        }
    }
}