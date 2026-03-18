package edu.oregonstate.cs492.MovieWatchListManager.data

import android.content.Context
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import edu.oregonstate.cs492.MovieWatchListManager.R
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
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "release_date") val releaseDate: String,
    val title: String,
    //If there is a trailer or video associated with the move
    val video: Boolean,
    //List of genre ids
    @Json(name = "genre_ids") val genreIDs: List<Int>

) : Serializable

//Entity for the db (without genreids)
@Entity
data class MovieEntity(
    @PrimaryKey
    val movieId: Int,
    val overview: String,
    //Add to the end of this string: https://image.tmdb.org/t/p/original
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    //If there is a trailer or video associated with the move
    val video: Boolean,
    val genreIDs: List<Int> = emptyList()
) : Serializable

@Entity
data class WatchedHistoryEntity(
    @PrimaryKey
    val movieId: Int,
    val rating: Int
) : Serializable

data class WatchHistoryWithMovie(
    @Embedded val history: WatchedHistoryEntity,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "movieId"
    )
    val movie: MovieEntity
)

class Converters {

    @TypeConverter
    fun fromGenreList(genres: List<Int>): String {
        return genres.joinToString(",")
    }

    @TypeConverter
    fun toGenreList(data: String): List<Int> {
        if (data.isEmpty()) return emptyList()
        return data.split(",").map { it.toInt() }
    }
}


//Mapper extension function
fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        movieId = this.movieId,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video,
        genreIDs = this.genreIDs
    )
}

fun Movie.toHistory(rating: Int): WatchedHistoryEntity {
    return WatchedHistoryEntity(
        movieId = this.movieId,
        rating = rating
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        movieId = this.movieId,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video,
        genreIDs = this.genreIDs
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
        10752 to "War",
        37 to "Western"
    )

    private val genreColorMap = mapOf(
        28 to R.color.genre_action,
        12 to R.color.genre_adventure,
        16 to R.color.genre_animation,
        35 to R.color.genre_comedy,
        80 to R.color.genre_crime,
        99 to R.color.genre_documentary,
        18 to R.color.genre_drama,
        10751 to R.color.genre_family,
        14 to R.color.genre_fantasy,
        36 to R.color.genre_history,
        27 to R.color.genre_horror,
        10402 to R.color.genre_music,
        9648 to R.color.genre_mystery,
        10749 to R.color.genre_romance,
        878 to R.color.genre_scifi,
        10770 to R.color.genre_tvmovie,
        53 to R.color.genre_thriller,
        10752 to R.color.genre_war,
        37 to R.color.genre_western
    )

    //helper that retrieves the genre name from teh genre id.
    fun getGenreName(genreId: Int): String?{
        return genreMap[genreId]
    }

    fun getGenreNames(): Map<Int, String>{
        return genreMap
    }

    fun getGenreColors(): Map<Int, Int>{
        return genreColorMap
    }
}
