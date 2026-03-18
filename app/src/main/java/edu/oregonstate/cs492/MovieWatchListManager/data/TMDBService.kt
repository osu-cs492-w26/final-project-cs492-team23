package edu.oregonstate.cs492.MovieWatchListManager.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Put your key here:
val api_key = "3a8c9a521e095c421002d82eea9385a7"
interface TMDBService {


    //GET /movie/now_playing
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") key: String =  api_key
    ): Response<MovieResults>

    //GET /movie/popular
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") key: String =  api_key
    ): Response<MovieResults>

    //GET /movie/top_rated
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") key: String =  api_key
    ): Response<MovieResults>

    //GET /movie/upcoming
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") key: String =  api_key
    ): Response<MovieResults>

    //GET /search/movie
    @GET("search/movie")
    suspend fun searchForMovie(
        //query is required
        @Query("query") query: String,
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") key: String =  api_key
    ): Response<MovieResults>

    //GET /movie/{movie_id}/videos
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        //movie_id required
        @Path("movie_id") movieId: Int,
        @Query("language") lang: String = "en-US",
        @Query("api_key") key: String =  api_key
    ): Response<MovieVideos>

    //GET /movie/{movie_id}
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        //movie_id required
        @Path("movie_id") movieId: Int,
        @Query("language") lang: String = "en-US",
        @Query("api_key") key: String =  api_key
    ): Response<MovieDetails>

    //GET /movie/{movie_id}/watch/providers
    @GET("movie/{movie_id}/watch/providers")
    suspend fun getStreamingServices(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String =  api_key
    ): Response<StreamingServices>

    //GET /movie/{movie_id}/similar
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String =  api_key
    ): Response<MovieResults>

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