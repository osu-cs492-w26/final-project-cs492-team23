package edu.oregonstate.cs492.MovieWatchListManager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun  insert(movie: MovieEntity)

    @Delete
    suspend fun  delete(movie: MovieEntity)

    @Query("SELECT * FROM MovieEntity")
    fun getAllMoviesInList(): Flow<List<MovieEntity>>

}