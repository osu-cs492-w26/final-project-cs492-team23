package edu.oregonstate.cs492.MovieWatchListManager.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class MovieResults (
    val results: List<Movie>
)

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "id") val movieId: Int,
    val overview: String,
    //Add to the end of this string: https://image.tmdb.org/t/p/original
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "release_date") val releaseDate: String,
    val title: String,
    //If there is a trailer or video associated with the move
    val video: Boolean,
    //List of genre ids
    @Json(name = "genre_ids") val genreIDs: List<Int>

) : Serializable

//Entity for the db (without genreids)
@Entity
@JsonClass(generateAdapter = true)
data class MovieEntity(
    @PrimaryKey
    @Json(name = "id") val movieId: Int,
    val overview: String,
    //Add to the end of this string: https://image.tmdb.org/t/p/original
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "release_date") val releaseDate: String,
    val title: String,
    //If there is a trailer or video associated with the move
    val video: Boolean
) : Serializable


//Mapper extension function
fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        movieId = this.movieId,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video
    )
}


object Genre{
    //Hashmap that maps a genre_id(key) to a genre name(value)
    private val genreMap = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Sci-Fi",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War"
    )

    //helper that retrieves the genre name from teh genre id.
    fun getGenreName(genreId: Int): String?{
        return genreMap[genreId]
    }

    fun getGenreNames(ids: List<Int>): List<String>{
        return ids.mapNotNull { genreMap[it] }
    }
}
